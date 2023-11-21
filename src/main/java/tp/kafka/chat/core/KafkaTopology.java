package tp.kafka.chat.core;

import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.api.TimeoutEvent.Timeout;
import tp.kafka.chat.context.TopicProperties;

/**
 * Configuration class for defining Kafka Streams topology in the application.
 * This class sets up Kafka Streams for processing messages and events, including
 * global tables, streams, and transformations.
 */
@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaTopology {

    final StringSerde stringSerde;
    final KafkaProtobufSerde<BadWord> badWordSerde;
    final KafkaProtobufSerde<Message> messageSerde;
    final KafkaProtobufSerde<Timeout> timeoutSerde;
    final TopicProperties topics;
    final StreamsBuilder builder;

    /**
     * Creates a global table for BadWord messages.
     *
     * @return A global table for BadWord messages.
     */
    @Bean
    GlobalKTable<String, BadWord> badWordGlobalTable() {
        var topic = topics.getBadWords();

        var consumerConfig = Consumed
                .with(stringSerde, badWordSerde)
                .withName("badword-source");

        var materialization = Materialized.<String, BadWord, KeyValueStore<Bytes, byte[]>>as("badword-table");

        return builder.globalTable(topic, consumerConfig, materialization);
    }

    /**
     * Creates a KStream for Message events.
     *
     * @return A KStream for Message events.
     */
    @Bean
    KStream<String, Message> messageSourceStream() {
        var topic = topics.getChat();
        var consumerConfig = Consumed
                .with(stringSerde, messageSerde)
                .withName("message-source");

        return builder.stream(topic, consumerConfig);
    }

    /**
     * Creates a KTable for Timeout events.
     *
     * @param messageSourceStream The KStream of Message events.
     * @param badWordGlobalTable  The global table of BadWord messages.
     * @return A KTable for Timeout events.
     */
    @Bean
    KTable<String, Timeout> timeoutTable(KStream<String, Message> messageSourceStream, GlobalKTable<String, BadWord> badWordGlobalTable){
        var storeBuilder = Stores.keyValueStoreBuilder(
            Stores.persistentKeyValueStore("timeoutTable"),
            stringSerde, timeoutSerde);
        builder.addStateStore(storeBuilder);
        return messageSourceStream.process(() -> new TimeoutProcessor(badWordGlobalTable.queryableStoreName()), "timeoutTable")
            .toTable(Materialized.with(stringSerde, timeoutSerde));
    }

    /**
     * Augments the messageSourceStream by marking messages as modOnly if the user was timeouted.
     *
     * @param messageSourceStream The KStream of Message events.
     * @param timeoutTable        The KTable of timeouted users.
     * @return A KStream for enriched Message events.
     */
    @Bean 
    KStream<String, Message> modMessageStream(KStream<String, Message> messageSourceStream, KTable<String, Timeout> timeoutTable){
        return messageSourceStream
            .selectKey((k,v) -> v.getSender().getLogin())
            .repartition(Repartitioned.with(stringSerde, messageSerde))
            .leftJoin(timeoutTable, this::joiner, Joined.with(stringSerde, messageSerde, timeoutSerde))
            .selectKey((k,v) -> v.getChannel())
            .repartition(Repartitioned.with(stringSerde, messageSerde));
    }


    /**
     * Creates a KStream for filtered Message events.
     *
     * @param modMessageStream The KStream of modified Message events.
     * @return A KStream for filtered Message events. Does not contain messages only for mods.
     */
    @Bean 
    KStream<String, Message> filteredMessageStream(KStream<String, Message> modMessageStream){
        return modMessageStream.filterNot((k,v) -> v.getModOnly());
    }

    private Message joiner(Message message, Timeout timeout) {
        return message.toBuilder()
            .setModOnly(timeout != null)
            .build();
    }
}

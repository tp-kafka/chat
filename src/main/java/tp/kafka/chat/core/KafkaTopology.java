package tp.kafka.chat.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import tp.kafka.chat.api.BadWordEvent;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.api.TimeoutEvent.Timeout;
import tp.kafka.chat.context.TopicProperties;

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
    final TimeoutMapper timeoutMapper;

    @Bean
    GlobalKTable<String, BadWord> badWordGlobalTable() {
        var topic = topics.getBadWords();

        var consumerConfig = Consumed
                .with(stringSerde, badWordSerde)
                .withName("badword-source");

        var materialization = Materialized.<String, BadWord, KeyValueStore<Bytes, byte[]>>as("badword-table");

        return builder.globalTable(topic, consumerConfig, materialization);
    }

    @Bean
    KStream<String, Message> messageSourceStream() {
        var topic = topics.getChat();
        var consumerConfig = Consumed
                .with(stringSerde, messageSerde)
                .withName("message-source");

        return builder.stream(topic, consumerConfig);
    }

    @Bean
    KTable<String, Timeout> timeoutTable(KStream<String, Message> messageSourceStream, GlobalKTable<String, BadWord> badWordGlobalTable){
        var storeBuilder = Stores.keyValueStoreBuilder(
            Stores.persistentKeyValueStore("timeoutTable"),
            stringSerde, timeoutSerde);
        builder.addStateStore(storeBuilder);
        return messageSourceStream.process(() -> new TimeoutProcessor(badWordGlobalTable.queryableStoreName()), "timeoutTable")
            .toTable(Materialized.with(stringSerde, timeoutSerde));
    }

    @Bean 
    KStream<String, Message> modMessageStream(KStream<String, Message> messageSourceStream, KTable<String, Timeout> timeoutTable){
        return messageSourceStream
            .selectKey((k,v) -> v.getSender().getLogin())
            .repartition(Repartitioned.with(stringSerde, messageSerde))
            .leftJoin(timeoutTable, this::joiner, Joined.with(stringSerde, messageSerde, timeoutSerde))
            .selectKey((k,v) -> v.getChannel())
            .repartition(Repartitioned.with(stringSerde, messageSerde));
    }


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

package tp.kafka.chat.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import tp.kafka.chat.api.BadWordEvent;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.context.TopicProperties;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaTopology {

    final StringSerde stringSerde;
    final KafkaProtobufSerde<BadWord> badWordSerde;
    final KafkaProtobufSerde<Message> messageSerde;
    final TopicProperties topics;
    final StreamsBuilder builder;

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
    KStream<String, Message> messageSourceStream(){
        var topic = topics.getBadWords();
        var consumerConfig = Consumed
            .with(stringSerde, messageSerde)
            .withName("message-source");

        return builder.stream(topic, consumerConfig);
    }

    @Bean
    KStream<String, Message> filteredMessageStream(KStream<String, Message> messageSourceStream, GlobalKTable<String, BadWord> badWordGlobalTable, StreamsBuilderFactoryBean  streamsBuilder){
        return messageSourceStream.filter((k,v) -> {
            var table = streamsBuilder.getKafkaStreams().store(StoreQueryParameters.fromNameAndType(badWordGlobalTable.queryableStoreName(), QueryableStoreTypes.keyValueStore()));
            return StreamEx.of(table.all())
                .map(kv -> (BadWord)kv.value)
                .anyMatch(badWord -> v.getMessage().contains(badWord.getWord()));
        });
    }


}

package tp.kafka.chat.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;


import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.api.BadWordEvent;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.context.TopicProperties;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaTopology {

    final StringSerde stringSerde;
    final KafkaProtobufSerde<BadWord> badWordSerde;
    final TopicProperties topics;
    final StreamsBuilder builder;

    @Bean    
    GlobalKTable<String, BadWord> badWordSource() {
        var topic = topics.getOutgoing().getBadWords();

        var consumerConfig = Consumed
            .with(stringSerde, badWordSerde)
            .withName("badword-source");
        
        var materialization = Materialized.<String, BadWord, KeyValueStore<Bytes, byte[]>>as("badword-table");
            
        return builder.globalTable(topic, consumerConfig, materialization);
    }
}

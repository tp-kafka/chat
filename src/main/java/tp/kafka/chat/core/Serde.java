package tp.kafka.chat.core;

import java.util.HashMap;

import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import tp.kafka.chat.api.BadWordEvent.BadWord;

@Configuration
public class Serde {
    
    @Bean
    StringSerde stringSerde() {
        return new StringSerde();
    }

    @Bean
    KafkaProtobufSerde<BadWord> badWordSerde(KafkaStreamsConfiguration config){
        var serdeConfig = new HashMap<String, Object>();
        config.asProperties().entrySet().forEach(e -> serdeConfig.put((String)e.getKey(), e.getValue()));
        var protobufSerde = new KafkaProtobufSerde<>(BadWord.class);
        protobufSerde.configure(serdeConfig, false);
        return protobufSerde;
    }
}

package tp.kafka.chat.core;

import java.util.HashMap;

import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.api.TimeoutEvent.Timeout;

/**
 * Configuration class for defining Kafka Serdes (serializers/deserializers)
 * used in the application.
 * This class provides Spring beans for creating Kafka Serdes for different
 * message types.
 */
@Configuration
public class Serde {

    /**
     * Creates and configures a Kafka Serde for strings.
     *
     * @return A Kafka Serde for strings.
     */
    @Bean
    StringSerde stringSerde() {
        return new StringSerde();
    }

    /**
     * Creates and configures a Kafka Serde for BadWord messages.
     *
     * @param config The Kafka Streams configuration.
     * @return A Kafka Serde for BadWord messages.
     */
    @Bean
    KafkaProtobufSerde<BadWord> badWordSerde(KafkaStreamsConfiguration config) {
        var serdeConfig = new HashMap<String, Object>();
        config.asProperties().entrySet().forEach(e -> serdeConfig.put((String) e.getKey(), e.getValue()));
        var protobufSerde = new KafkaProtobufSerde<>(BadWord.class);
        protobufSerde.configure(serdeConfig, false);
        return protobufSerde;
    }

    /**
     * Creates and configures a Kafka Serde for Message events.
     *
     * @param config The Kafka Streams configuration.
     * @return A Kafka Serde for Message events.
     */
    @Bean
    KafkaProtobufSerde<Message> messageSerde(KafkaStreamsConfiguration config) {
        var serdeConfig = new HashMap<String, Object>();
        config.asProperties().entrySet().forEach(e -> serdeConfig.put((String) e.getKey(), e.getValue()));
        var protobufSerde = new KafkaProtobufSerde<>(Message.class);
        protobufSerde.configure(serdeConfig, false);
        return protobufSerde;
    }

    /**
     * Creates and configures a Kafka Serde for Timeout events.
     *
     * @param config The Kafka Streams configuration.
     * @return A Kafka Serde for Timeout events.
     */
    @Bean
    KafkaProtobufSerde<Timeout> timeoutSerde(KafkaStreamsConfiguration config) {
        var serdeConfig = new HashMap<String, Object>();
        config.asProperties().entrySet().forEach(e -> serdeConfig.put((String) e.getKey(), e.getValue()));
        var protobufSerde = new KafkaProtobufSerde<>(Timeout.class);
        protobufSerde.configure(serdeConfig, false);
        return protobufSerde;
    }
}

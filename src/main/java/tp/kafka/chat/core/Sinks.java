package tp.kafka.chat.core;

import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.context.TopicProperties;

/**
 * Component class for configuring Kafka stream sinks in the application.
 * This class defines how filtered messages are sent to a Kafka topic.
 */
@Component
@RequiredArgsConstructor
public class Sinks {
    
    final KStream<String, Message> filteredMessageStream;
    final TopicProperties topics;
    final StringSerde stringSerde;
    final KafkaProtobufSerde<Message> messageSerde;

    @PostConstruct
    void init(){
        filteredMessageStream.to(topics.getFilteredChat(), Produced.with(stringSerde, messageSerde));
    }
}

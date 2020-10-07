package tp.kafka.chat;

import static java.util.Optional.empty;

import java.util.Optional;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import lombok.extern.log4j.Log4j2;

import org.springframework.kafka.support.KafkaHeaders;

@TestComponent
@Log4j2
public class KafkaTestConsumer {

    public Optional<String> consumedString;
    public Optional<String> consumedTopic;
    public Optional<Integer> consumedPartition;

    public KafkaTestConsumer(){
        this.reset();
    }

    public void reset() {
        consumedString = empty();
        consumedTopic = empty();
        consumedPartition = empty();
    }

    //TODO: Consume a simple string payload from topic "consumeString"
    // by using the KafkaListener annotation
    public void consumeString(String str){
        consumedString = Optional.of(str);
        KafkaTestConsumer.log.info("consumeString: {}", str);
    }

    //TODO: Consume topic and partition (on which the message was received) as well 
    // as the string payload from topic "consumeMetadata" by using the KafkaListener, 
    // Payload and Header annotation and the KafkaHeaders class
    public void consumeMetadata(
       String value, 
       String topic, 
       Integer partition
    ){
        consumedString = Optional.of(value);
        consumedTopic = Optional.of(topic);
        consumedPartition = Optional.of(partition);
        KafkaTestConsumer.log.info("consumeMetadata: {},{},{}", value, topic, partition);
    }


}

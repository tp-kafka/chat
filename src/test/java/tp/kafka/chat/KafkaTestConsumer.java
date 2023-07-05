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

    @KafkaListener(topics = "consumeString")
    public void consumeString(String str){
        consumedString = Optional.of(str);
        KafkaTestConsumer.log.info("consumeString: {}", str);
    }

    @KafkaListener(topics = "consumeMetadata")
    public void consumeMetadata(
        @Payload String value, 
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, 
        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition
    ){
        consumedString = Optional.of(value);
        consumedTopic = Optional.of(topic);
        consumedPartition = Optional.of(partition);
        KafkaTestConsumer.log.info("consumeMetadata: {},{},{}", value, topic, partition);
    }


}

package tp.kafka.chat.api;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class that listens to Kafka messages.
 * This class is responsible for handling incoming messages from a Kafka topic.
 */
@Service
@Slf4j
public class MessageKafkaApi {
    
    /**
     * Listens to Kafka messages on the specified topic.
     * When a message is received, it logs the message along with the sender's information and channel.
     *
     * @param record The Kafka ConsumerRecord containing the message data and metadata.
     */
    //TODO: use KafkaListener Annotation to consume ${topics.chat}
    //see: https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages/listener-annotation.html
    public void listen(ConsumerRecord<String, Message> record) {
        var channel = "TODO: get the channel from the message";
        var nick = "TODO: get the senders nick";
        var message = "TODO: get the message contained in the message";
        
        log.info("[{}] received message: ({}) {}", channel, nick, message);
    }

}

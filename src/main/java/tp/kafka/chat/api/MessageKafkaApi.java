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
    @KafkaListener(topics = "${topics.chat}")
    public void listen(ConsumerRecord<String, Message> record) {
        var channel = record.value().getChannel();
        var nick = record.value().getSender().getNick();
        var message = record.value().getMessage();
        
        log.info("[{}] received message: ({}) {}", channel, nick, message);
    }

}

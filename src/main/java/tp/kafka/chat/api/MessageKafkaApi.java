package tp.kafka.chat.api;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageKafkaApi {
    
    @KafkaListener(topics = "${topics.chat}")
    public void listen(ConsumerRecord<String, Message> record) {
        var channel = record.value().getChannel();
        var nick = record.value().getSender().getNick();
        var message = record.value().getMessage();
        
        log.info("[{}] received message: ({}) {}", channel, nick, message);
    }

}

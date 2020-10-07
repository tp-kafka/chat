package tp.kafka.chat;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class KafkaAdapter {

    private String inputTopic;
    private KafkaTemplate<Object, ChatMessage> kafka;
    
    public KafkaAdapter(@Value("${chat.topics.out.input}") String inputTopic, KafkaTemplate<Object, ChatMessage> kafka){
        this.kafka = kafka;
        this.inputTopic = inputTopic;
    }

    public void sendChatMessage(ChatMessage chatMessage) throws InterruptedException, ExecutionException {
        //TODO send chatmessage serialized as JSON to inputTopic
    }

    //TODO receive chatmessage (annotate here)
    public void receiveChatMessage(ChatMessage message) {
        var username = message.getUserId();
        var text = message.getMessage();
        KafkaAdapter.log.info("{}> {}", username, text);
    }

}
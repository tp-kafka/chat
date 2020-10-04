package tp.kafka.chat;

import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaAdapter {
    private KafkaTemplate<Object, ChatMessage> kafka;

    public void sendChatMessage(ChatMessage chatMessage) throws InterruptedException, ExecutionException {
        var result = kafka.send("chat", chatMessage).get();
    }
}
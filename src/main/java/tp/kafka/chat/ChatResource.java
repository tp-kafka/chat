package tp.kafka.chat;

import java.util.concurrent.ExecutionException;

import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ChatResource
 */
@RestController
@AllArgsConstructor
@Log4j2
public class ChatResource {

    private KafkaTemplate<Object, ChatMessage> kafka;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@CookieValue(value = "username") String username, @RequestBody String msg)
            throws InterruptedException, ExecutionException {
        var chatMessage = new ChatMessage(username, msg);
        var result = kafka.send("chat", chatMessage);
        ChatResource.log.info(chatMessage);
        result.get();
    }
}
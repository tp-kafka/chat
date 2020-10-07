package tp.kafka.chat;

import java.util.concurrent.ExecutionException;

import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ChatResource
 */
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/chat")
public class ChatResource {

    private KafkaAdapter kafka;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestHeader("username") String username, @RequestBody String msg)
            throws InterruptedException, ExecutionException {
        var chatMessage = new ChatMessage(username, msg);
        kafka.sendChatMessage(chatMessage);
        ChatResource.log.info("Rest Request forwareded to Kafka: {}", chatMessage);
    }
}
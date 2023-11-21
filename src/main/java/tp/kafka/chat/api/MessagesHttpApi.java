package tp.kafka.chat.api;

import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tp.kafka.chat.api.model.ChatMessage;
import tp.kafka.chat.context.TopicProperties;

/**
 * Http-Service class for sending chat messages using Kafka Streams.
 * This class provides http-endpoints to create, delete, and read bad words stored in a Kafka topic.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MessagesHttpApi implements MessagesApiDelegate {
    
    final KafkaTemplate<String, Message> kafkaTemplate;
    final TopicProperties topicProperties;
    final MessageMapper messageMapper;

    @Override
    @SneakyThrows
    public ResponseEntity<Void> sendMessage(ChatMessage chatMessage) {
        log.info("sending chatMessage {}", chatMessage);
        var message = messageMapper.toProtobuf(chatMessage);
        var key = message.getChannel();
        var topic = topicProperties.getChat();
        kafkaTemplate.send(topic, key, message).get(2, TimeUnit.SECONDS);
        return ResponseEntity.accepted().build();
    }
}

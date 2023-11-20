package tp.kafka.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tp.kafka.chat.api.model.ChatMessage;
import tp.kafka.chat.context.TopicProperties;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessagesHttpApi implements MessagesApiDelegate {
    
    final KafkaTemplate<String, Message> kafkaTemplate;
    final TopicProperties topicProperties;
    final MessageMapper messageMapper;

    @Override
    public ResponseEntity<Void> sendMessage(ChatMessage chatMessage) {
        log.info("sending chatMessage {}", chatMessage);
        var message = messageMapper.toProtobuf(chatMessage);
        var key = message.getChannel();
        var topic = topicProperties.getOutgoing().getChat();
        kafkaTemplate.send(topic, key, message);
        return ResponseEntity.accepted().build();
    }
}

package tp.kafka.chat.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tp.kafka.chat.api.model.ChatMessage;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessagesApiImpl implements MessagesApiDelegate {
    
    @Override
    public ResponseEntity<Void> sendMessage(ChatMessage chatMessage) {
        log.info("sending chatMessage {}", chatMessage);
        return ResponseEntity.accepted().build();
    }
}

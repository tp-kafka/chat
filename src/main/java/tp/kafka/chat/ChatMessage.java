package tp.kafka.chat;

import lombok.Value;

@Value
public class ChatMessage {
    private String userId;
    private String message;
}
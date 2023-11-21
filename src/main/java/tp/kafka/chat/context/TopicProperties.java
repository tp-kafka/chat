package tp.kafka.chat.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Component
@ConfigurationProperties("topics")
@Data
@Validated
public class TopicProperties {

        @NotEmpty
        String chat;
        @NotEmpty
        String filteredChat;
        @NotEmpty
        String badWords;
        @NotEmpty
        String timeouts;

}

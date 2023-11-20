package tp.kafka.chat.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Component
@ConfigurationProperties("topics")
@Data
@Validated
public class TopicProperties {

    @Data
    public static final class Incoming {
        @NotEmpty
        String chat;
    }

    @Data
    public static final class Outgoing {
        @NotEmpty
        String chat;
    }

    @NotNull
    Incoming incoming;

    @NotNull
    Outgoing outgoing;
}

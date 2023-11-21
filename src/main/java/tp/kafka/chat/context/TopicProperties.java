package tp.kafka.chat.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Configuration properties class for defining Kafka topics used in the
 * application.
 */
@Component
@ConfigurationProperties("topics")
@Data
@Validated
public class TopicProperties {

        /**
         * The name of the Kafka topic for chat messages.
         */
        @NotEmpty
        String chat;

        /**
         * The name of the Kafka topic for filtered chat messages.
         */
        @NotEmpty
        String filteredChat;

        /**
         * The name of the Kafka topic for bad words events.
         */
        @NotEmpty
        String badWords;

        /**
         * The name of the Kafka topic for timeout notifications.
         */
        @NotEmpty
        String timeouts;

}

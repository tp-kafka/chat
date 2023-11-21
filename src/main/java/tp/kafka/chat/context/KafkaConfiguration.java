package tp.kafka.chat.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Configuration class for setting up Kafka in the Spring application context.
 * This class is annotated with {@code @Configuration} and {@code @EnableKafka} to enable Kafka support.
 */
@Configuration
@EnableKafka
public class KafkaConfiguration {
    
}

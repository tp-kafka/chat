package tp.kafka.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.ONE_SECOND;
import static org.awaitility.Durations.TEN_SECONDS;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@EmbeddedKafka(topics = { "consumeString",
        "consumeMetadata" }, bootstrapServersProperty = "spring.kafka.properties.bootstrap.servers")
@Log4j2
@Disabled("Dieser Test ist aus einer vorherigen Übung")
public class ConsumeExcerciseTest {

    @TestConfiguration
    public static class Context {
        @Bean
        public KafkaTestConsumer cosnumer() {
            return new KafkaTestConsumer();
        }
    }

    @Autowired
    KafkaTemplate<String, String> kafka;

    @Autowired
    KafkaTestConsumer consumer;

    @BeforeEach
    public void resetConsumer() {
        consumer.reset();
    }

    @Test
    public void consumeString() throws Exception {
        // arrange
        var message = "Hello consumer";
        var topic = "consumeString";
        kafka.send(topic, message);

        // assert
        await().atMost(TEN_SECONDS).untilAsserted(() -> {
            ConsumeExcerciseTest.log.info("checking consumeString: {} {} {}" , 
                consumer.consumedString, consumer.consumedTopic, consumer.consumedPartition);
            assertThat(consumer.consumedString).hasValue(message);
            assertThat(consumer.consumedTopic).isEmpty();
            assertThat(consumer.consumedPartition).isEmpty();
        });
    }

    @Test 
    public void consumeMetadata() throws Exception {
        //arrange
        var message = "Hello metadata";
        var topic = "consumeMetadata";
        kafka.send(topic, message);

        //assert
        await().atMost(TEN_SECONDS).untilAsserted(() -> {
            ConsumeExcerciseTest.log.info("checking consumeMetadata: {} {} {}" , 
                consumer.consumedString, consumer.consumedTopic, consumer.consumedPartition);
            assertThat(consumer.consumedString).hasValue(message);
            assertThat(consumer.consumedTopic).hasValue(topic);
            assertThat(consumer.consumedPartition).isNotEmpty();
        });
    }

}
package tp.kafka.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest
@EmbeddedKafka(topics = { "string", "stringWithKey",
        "stringToPartition" }, bootstrapServersProperty = "spring.kafka.properties.bootstrap.servers")
@Disabled("Dieser Test ist aus einer vorherigen Übung")
class ProduceExcerciseTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    KafkaTemplate<String, String> kafka;

    public Map<String, Object> consumerProps() {
        var consumerProps = KafkaTestUtils.consumerProps("ProduceExcercise", "true", this.embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return consumerProps;
    }

    @Test
    void sendString() throws Exception {
        // arrange
        var topic = "string";
        var teststring = "Hello Kafka!";

        // act
        kafka.send(topic, teststring);

        // assert
        var cf = new DefaultKafkaConsumerFactory<String, String>(consumerProps());
        var consumer = cf.createConsumer("sendString", "sendString");
        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
        ConsumerRecords<String, String> messages = KafkaTestUtils.getRecords(consumer, 5000l);
        assertThat(messages.count())
            .as("we should receive at least one message").isGreaterThanOrEqualTo(1);
        assertThat(messages).extracting(ConsumerRecord::value)
            .allMatch(val -> teststring.equals(val), "we should receive the teststring");
        assertThat(messages).extracting(ConsumerRecord::key)
            .allMatch(key -> key == null, "we should not receive a key");
    }

    @Test
    void sendStringWithKey() throws Exception {
        // arrange
        var key = "key";
        var topic = "stringWithKey";
        var teststring = "Hello Kafka Keys!";

        // act
        kafka.send(topic, key, teststring);

        // assert
        var cf = new DefaultKafkaConsumerFactory<String, String>(consumerProps());
        var consumer = cf.createConsumer("sendStringWithKey", "sendStringWithKey");
        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
        ConsumerRecords<String, String> messages = KafkaTestUtils.getRecords(consumer, 5000l);
        assertThat(messages.count())
            .as("we should receive at least one message").isGreaterThanOrEqualTo(1);
        assertThat(messages).extracting(ConsumerRecord::value)
            .allMatch(val -> teststring.equals(val), "we should receive the teststring");
        assertThat(messages).extracting(ConsumerRecord::key)
            .allMatch(k -> key.equals(key), "we should receive the original key");
    }

    @Test
    void stringToPartition() throws Exception {
        // arrange
        var topic = "stringToPartition";
        var p0 = "Send this to partition 0";
        var p1 = "Send this to partition 1";

        // act
        kafka.send(topic, 0, null, p0);
        kafka.send(topic, 1, null, p1);

        // assert
        var cf = new DefaultKafkaConsumerFactory<String, String>(consumerProps());
        var consumer = cf.createConsumer("stringToPartition", "stringToPartition");
        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
        ConsumerRecords<String, String> messages = KafkaTestUtils.getRecords(consumer, 5000l);
        assertThat(messages.count()).as("we should receive at least two messages").isGreaterThanOrEqualTo(2);
        assertThat(messages).extracting(ConsumerRecord::value)
            .as("should receive our teststrings").contains(p0,p1);
        assertThat(messages).extracting("partition", "value")
            .as("messages should be in the correct partition").contains(tuple(0, p0), tuple(1, p1));

    }

}

package tp.kafka.chat.core;

import java.io.File;
import java.io.FileReader;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import tp.kafka.chat.assertions.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;
import com.google.protobuf.util.JsonFormat;

import lombok.SneakyThrows;
import tp.kafka.chat.api.BadWordEvent.BadWord;

/**
 * Test class for Kafka Topology.
 * It provides unit testing for various scenarios involving Kafka Streams processing.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
@ActiveProfiles("test")
public class KafkaTopologyTest {

    @Autowired
    TestInputTopic<String, Message> messageInput;
    @Autowired
    TestInputTopic<String, BadWord> badWordInput;
    @Autowired
    TestOutputTopic<String, Message> messageOutput;

    @Test
    public void messages_shouldBeTransmitted(SoftAssertions softly) {
        // arrange
        var okMessage = Message.newBuilder();
        loadJson("chatMessage.ok.json", okMessage);

        // act
        //TODO: use messageInput to pipe in okMessage

        // assert
        softly.assertThat(messageOutput.getQueueSize()).isEqualTo(1);
        //TODO: check value of message output
    }

    @Test
    public void messages_shouldBeFiltered_IfContainingABadWord(SoftAssertions softly) {
        // arrange
        var forbiddenMessage = Message.newBuilder();
        loadJson("chatMessage.forbidden.json", forbiddenMessage);

        // act
        badWordInput.pipeInput("filtered", BadWord.newBuilder().setWord("filtered").build());
        messageInput.pipeInput(forbiddenMessage.getChannel(), forbiddenMessage.build());

        // assert
        //TODO: check message was filtered
    }

    @Test
    public void messages_shouldBeFiltered_IfUserIsTimedOut(SoftAssertions softly) {
        // arrange
        var okMessage = Message.newBuilder();
        loadJson("chatMessage.ok.json", okMessage);
        var forbiddenMessage = Message.newBuilder();
        loadJson("chatMessage.forbidden.json", forbiddenMessage);

        // act
        badWordInput.pipeInput("filtered", BadWord.newBuilder().setWord("filtered").build());
        messageInput.pipeInput(forbiddenMessage.getChannel(), forbiddenMessage.build());
        messageInput.pipeInput(okMessage.getChannel(), okMessage.build());

        // assert
        //TODO: check message was filtered
    }

    /**
     * Loads a JSON file into a protobuf message builder.
     * This method is used to load test data from a JSON file.
     *
     * @param file The file name of the JSON file to load.
     * @param builder The protobuf message builder to load data into.
     * @param <T> The type of the protobuf message builder.
     * @throws Exception if any IO or parsing errors occur.
     */
    @SneakyThrows
    <T extends com.google.protobuf.GeneratedMessageV3.Builder<?>> void loadJson(String file, T builder) {
        var resource = KafkaTopologyTest.class.getResource("/data/" + file);
        var fileReader = new FileReader(new File(resource.toURI()));
        JsonFormat.parser().merge(fileReader, builder);
    }

}

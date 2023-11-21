package tp.kafka.chat.core.context;

import org.apache.kafka.common.serialization.Serdes.StringSerde;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.context.TopicProperties;

@Configuration
@RequiredArgsConstructor
public class TopicConfiguration {
  
  final TopologyTestDriver driver;
  final TopicProperties topics;
  final StringSerde stringSerde;
  
  @Bean
  public TestInputTopic<String, Message> messageInput(KafkaProtobufSerde<Message> messageSerde){
      return driver.createInputTopic(topics.getChat(), stringSerde.serializer(), messageSerde.serializer());
  }

  @Bean 
  TestInputTopic<String, BadWord> badWordInput(KafkaProtobufSerde<BadWord> badWordSerde){
      return driver.createInputTopic(topics.getBadWords(), stringSerde.serializer(), badWordSerde.serializer());
  }

  @Bean 
  TestOutputTopic<String, Message> messageOutput(KafkaProtobufSerde<Message> messageSerde){
      return driver.createOutputTopic(topics.getFilteredChat(), stringSerde.deserializer(), messageSerde.deserializer());
  }

}

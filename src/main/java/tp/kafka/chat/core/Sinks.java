package tp.kafka.chat.core;

import org.apache.kafka.streams.kstream.KStream;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.context.TopicProperties;

@RequiredArgsConstructor
public class Sinks {
    
    final KStream<String, Message> filteredMessageStream;
    final TopicProperties topics;

    @PostConstruct
    void init(){
        filteredMessageStream.to(topics.getFilteredChat());
    }
}

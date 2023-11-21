package tp.kafka.chat.core;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import tp.kafka.chat.api.TimeoutEvent.Timeout;
import tp.kafka.chat.context.TopicProperties;

@Component
@RequiredArgsConstructor
public class Sinks {
    
    final KStream<String, Message> filteredMessageStream;
    final TopicProperties topics;

    @PostConstruct
    void init(){
        filteredMessageStream.to(topics.getFilteredChat());
    }
}

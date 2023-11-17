package tp.kafka.chat.api;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;
import com.google.protobuf.Timestamp;

import tp.kafka.chat.api.model.ChatMessage;

@Mapper
public interface MessageMapper {
    @Mapping(target = "createdat", expression = "java(now())")
    @Mapping(target = "sender.nick", source = "nick")
    @Mapping(target = "sender.hostname", constant = "localhost")
    @Mapping(target = "sender.login", source="nick")
    Message toProtobuf(ChatMessage chatMessage);

    default Timestamp now(){
        var time = Instant.now();
        return Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
    }
}

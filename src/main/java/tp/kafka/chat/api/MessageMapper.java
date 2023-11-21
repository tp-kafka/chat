package tp.kafka.chat.api;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;
import com.google.protobuf.Timestamp;

import tp.kafka.chat.api.model.ChatMessage;

/**
 * Mapper interface for transforming {@link ChatMessage} objects to {@link Message} protobuf objects.
 * Utilizes MapStruct for object mapping.
 */
@Mapper
public interface MessageMapper {

    /**
     * Transforms a {@link ChatMessage} to a {@link Message} protobuf object.
     * Sets the 'createdat' field to the current timestamp, 'sender.nick' and 'sender.login' to the 'nick' field of the source,
     * and 'sender.hostname' to 'localhost'.
     *
     * @param chatMessage The chat message to be transformed.
     * @return The transformed protobuf {@link Message} object.
     */
    @Mapping(target = "createdat", expression = "java(now())")
    @Mapping(target = "sender.nick", source = "nick")
    @Mapping(target = "sender.hostname", constant = "localhost")
    @Mapping(target = "sender.login", source="nick")
    @Mapping(target = "modOnly", ignore = true)
    Message toProtobuf(ChatMessage chatMessage);

    /**
     * Helper method to generate the current timestamp as a {@link Timestamp} protobuf object.
     *
     * @return The current {@link Timestamp}.
     */
    default Timestamp now(){
        var time = Instant.now();
        return Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano()).build();
    }
}

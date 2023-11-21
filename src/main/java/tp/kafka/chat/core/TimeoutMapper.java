package tp.kafka.chat.core;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import tp.kafka.chat.api.TimeoutEvent.Timeout;

@Mapper
public abstract class TimeoutMapper {
    
    @Mapping(target = "login", source = "sender.login")
    @Mapping(target = "until", expression = "java(timeout30s())")
    abstract Timeout createTimeout(Message fromMessage);

    Instant parse(String string) {
        return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(string));
    }

    @Named("timeout30s")
    protected String timeout30s(){
        var until = Instant.now().plusSeconds(30);
        return DateTimeFormatter.ISO_INSTANT.format(until);
    }
}

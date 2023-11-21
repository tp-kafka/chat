package tp.kafka.chat.core;

import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.TimestampedKeyValueStore;
import org.mapstruct.factory.Mappers;

import com.github.cjmatta.kafka.connect.irc.MessageEvent.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.api.TimeoutEvent.Timeout;

import java.time.Duration;
import java.time.Instant;

import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.ValueAndTimestamp;

@Slf4j
@RequiredArgsConstructor
public class TimeoutProcessor implements Processor<String, Message, String, Timeout>, Punctuator{
    final String badWordTableName;
    TimestampedKeyValueStore<String, BadWord> badWordTable;
    KeyValueStore<String, Timeout> timeoutTable;
    ProcessorContext<String, Timeout> context;
    TimeoutMapper mapper = Mappers.getMapper(TimeoutMapper.class);

    @Override
    public void init(ProcessorContext<String, Timeout> context) {
        Processor.super.init(context);
        this.context = context;
        this.badWordTable = (TimestampedKeyValueStore<String, BadWord>) context.getStateStore(badWordTableName);
        this.timeoutTable = (KeyValueStore<String, Timeout>) context.getStateStore("timeoutTable");
        context.schedule(Duration.ofSeconds(10), PunctuationType.WALL_CLOCK_TIME, this);
    }

    @Override
    public void process(Record<String, Message> record) {
        var msg = record.value();
        if(containsBadWord(msg)){
            var newRecord = record
                .withKey(msg.getSender().getLogin())
                .withValue(mapper.createTimeout(msg));
            timeoutTable.put(newRecord.key(), newRecord.value());
            context.forward(newRecord);
            log.info("{} was timed out until {}", newRecord.value().getLogin(), newRecord.value().getUntil());
        }
    }

    protected boolean containsBadWord(Message message){
        return StreamEx.of(badWordTable.all())
            .map(kv -> kv.value)
            .map(vt -> vt.value())
            .peek(badWord -> log.debug("checking {} for {}", message.getMessage(), badWord))
            .anyMatch(badWord -> message.getMessage().contains(badWord.getWord()));
    }

    @Override
    public void punctuate(long timestamp) {
        log.debug("clearing expired timeouts");
        var now = Instant.ofEpochMilli(timestamp);
        timeoutTable.all().forEachRemaining( entry -> {
            var expiry = mapper.parse(entry.value.getUntil());
            var expired = now.isAfter(expiry);
            if(expired){
                log.info("timeout for {} is expired. (now ({}) is after {}) ", entry.key, now, expiry);
                timeoutTable.delete(entry.key);
                context.forward(new Record<String, Timeout>(entry.key, null, timestamp));
            }
        });
    }
}

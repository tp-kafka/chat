package tp.kafka.chat.core;

import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
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
    ReadOnlyKeyValueStore<String, BadWord> badWordTable;
    KeyValueStore<String, Timeout> timeoutTable;
    ProcessorContext<String, Timeout> context;
    TimeoutMapper mapper = Mappers.getMapper(TimeoutMapper.class);

    @Override
    public void init(ProcessorContext<String, Timeout> context) {
        Processor.super.init(context);
        this.context = context;
        this.badWordTable = (ReadOnlyKeyValueStore<String, BadWord>) context.getStateStore(badWordTableName);
        this.timeoutTable = (KeyValueStore<String, Timeout>) context.getStateStore("timeoutTable");
        context.schedule(Duration.ofSeconds(10), PunctuationType.STREAM_TIME, this);
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
        }
    }

    protected boolean containsBadWord(Message message){
        return StreamEx.of(badWordTable.all())
            .map(kv -> (ValueAndTimestamp)kv.value)
            .map(vt -> vt.)
            .peek(badWord -> log.info("checking {} for {}", message.getMessage(), badWord))
            .anyMatch(badWord -> message.getMessage().contains(badWord.getWord()));
    }

    @Override
    public void punctuate(long timestamp) {
        log.info("clearing expired timeouts");
        var now = Instant.ofEpochMilli(timestamp);
        timeoutTable.all().forEachRemaining( entry -> {
            var expired = mapper.parse(entry.value.getUntil()).isAfter(now);
            if(expired){
                log.info("timeout for {} is expired.", entry.key);
                timeoutTable.delete(entry.key);
                context.forward(new Record<String, Timeout>(entry.key, null, timestamp));
            }
        });

    }
}

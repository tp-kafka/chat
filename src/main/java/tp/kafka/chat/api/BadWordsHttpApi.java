package tp.kafka.chat.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import tp.kafka.chat.api.BadWordEvent.BadWord;
import tp.kafka.chat.api.model.CreateBadWord;
import tp.kafka.chat.api.model.DeleteBadWord;
import tp.kafka.chat.api.model.ReadBadWord;
import tp.kafka.chat.context.TopicProperties;

/**
 * Http-Service class for managing bad words using Kafka Streams.
 * This class provides endpoints to create, delete, and read bad words stored in a Kafka topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BadWordsHttpApi implements BadwordsApiDelegate {
    final KafkaTemplate<String, BadWord> kafkaTemplate;
    final TopicProperties topicProperties;
    @Nullable final StreamsBuilderFactoryBean  streamsBuilder;
    @Nullable final GlobalKTable<String, BadWord> badWordGlobalTable;

    @Override
    @SneakyThrows
    public ResponseEntity<Void> createBadWord(CreateBadWord createBadWord) {
        var topic = topicProperties.getBadWords();
        var results = StreamEx.of(createBadWord.getWordlist())
            .peek(word -> log.info("creating bad word {}", word))
            .map(word -> BadWord.newBuilder().setWord(word).build())
            .map(word -> kafkaTemplate.send(topic, word.getWord(), word))
            .toArray(CompletableFuture.class);

        CompletableFuture.allOf(results).get(2, TimeUnit.SECONDS);
        return ResponseEntity.accepted().build();    
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Void> deleteBadWord(DeleteBadWord deleteBadWord) {
        var topic = topicProperties.getBadWords();
        var results = StreamEx.of(deleteBadWord.getWordlist())
            .peek(word -> log.info("deleting bad word {}", word))
            .map(word -> BadWord.newBuilder().setWord(word).build())
            .map(word -> kafkaTemplate.send(topic, word.getWord(), null))
            .toArray(CompletableFuture.class);
        
        CompletableFuture.allOf(results).get(2, TimeUnit.SECONDS);
        return ResponseEntity.accepted().build();    
    }

    @Override
    public ResponseEntity<ReadBadWord> readBadWord() {
        var table = streamsBuilder.getKafkaStreams().store(StoreQueryParameters.fromNameAndType(badWordGlobalTable.queryableStoreName(), QueryableStoreTypes.keyValueStore()));
        var badWordList = StreamEx.of(table.all())
            .map(kv -> (BadWord)kv.value)
            .map(word -> word.getWord())
            .toList();
        var badWord = new ReadBadWord();
        badWord.setWordlist(badWordList);
        return ResponseEntity.ok().body(badWord);
    }
}
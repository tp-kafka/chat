package tp.kafka.chat.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import tp.kafka.chat.api.model.CreateBadWord;
import tp.kafka.chat.api.model.DeleteBadWord;
import tp.kafka.chat.context.TopicProperties;
import tp.lkafka.chat.api.BadWordEvent.BadWord;

/**
 * BadWordsHttpApi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BadWordsHttpApi implements BadwordsApiDelegate {
    final KafkaTemplate<String, BadWord> kafkaTemplate;
    final TopicProperties topicProperties;

    @Override
    @SneakyThrows
    public ResponseEntity<Void> createBadWord(CreateBadWord createBadWord) {
        var topic = topicProperties.getOutgoing().getBadWords();
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
        var topic = topicProperties.getOutgoing().getBadWords();
        var results = StreamEx.of(deleteBadWord.getWordlist())
            .peek(word -> log.info("deleting bad word {}", word))
            .map(word -> BadWord.newBuilder().setWord(word).build())
            .map(word -> kafkaTemplate.send(topic, word.getWord(), null))
            .toArray(CompletableFuture.class);
        
        CompletableFuture.allOf(results).get(2, TimeUnit.SECONDS);
        return ResponseEntity.accepted().build();    
    }
}
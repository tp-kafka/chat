package tp.kafka.chat.core.context;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TopologyTestDriver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * waits for stream creation and creates the TopologyTestDriver.
 */
@Configuration
@RequiredArgsConstructor
public class TopologyDriverConfiguration {

    final StreamsBuilder builder;

    @Bean
    @DependsOn("sinks")
    TopologyTestDriver topologyDriver() {
        val topology = builder.build();

        return new TopologyTestDriver(topology);
    }
}

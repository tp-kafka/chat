package tp.kafka.chat.core.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;

/**
 * Replaces the schema-registry client with a mock
 */
@Configuration
public class MockSchemaRegistryConfiguration {

    @Bean
    @Primary
    public SchemaRegistryClient mockedSchemaRegistryClient() {
        return new MockSchemaRegistryClient();
    }
}

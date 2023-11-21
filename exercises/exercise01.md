# Kafka API Training Exercise in Spring Boot

## Goals

- Learn to use Kafka API within a Spring Boot application.
- Explore Kafka UI to understand message distribution across partitions and instances.

## Preparation

- Set up your IDE as per the instructions in `README.md`.

## Overview

- The exercise includes a Spring Boot application with an HTTP endpoint to send chat messages.

## Tasks for Attendees

### Implementing Message Forwarding to Kafka

1. In `tp.kafka.chat.api.MessagesHttpApi`, locate the `sendMessage` method.
2. Utilize the `kafkaTemplate` (injected into the service) to send messages to Kafka:
    - Use the pre-calculated `topic`, `key`, and `value` in the method.
    - As the send method is asynchronous (returns a CompletableFuture):
        - Consider what happens if the app terminates before successful message delivery.
        - Modify the code to return HTTP 202 only after successful message delivery.

### Validating Your Work

- Verify the implementation through Kafka UI.

### Implementing Reading from Kafka

1. In `tp.kafka.chat.api.MessageKafkaApi`:
   - Apply the `KafkaListener` annotation to consume the topic defined in `topics.chat`.
   - Uncomment the log message and extract values from the Kafka record.
   - Discuss why this setup doesn't guarantee "read exactly once" and propose a solution for exactly once delivery.

### Message Distribution

In this section, attendees will explore how different message keys affect the distribution of messages to partitions in Kafka. This section will provide practical insights into Kafka's partitioning mechanism and its effects on application scalability and reliability. 

- Send messages with varying keys to Kafka.
- Observe and analyze how different keys influence the distribution across partitions.
- Examine how varying message distribution affects the application's logging.
- Start multiple instances of the application on different ports.
  - Use the command `mvn spring-boot:run` in separate terminal sessions for each instance.
  - change the port in the application.yml
- Investigate how running multiple instances affects message consumption and load distribution.
- Discuss the behavior when instances are connected to the same Kafka cluster.


## Predefined Configuration in `application.yml`

```yaml
producer:
  key-serializer: org.apache.kafka.common.serialization.StringSerializer
  value-serializer: io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer
consumer:
  group-id: chat-application
  key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
  value-deserializer: io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer
  properties:
    "specific.protobuf.value.type": com.github.cjmatta.kafka.connect.irc.MessageEvent$Message
properties:
  "schema.registry.url": http://localhost:8085
```
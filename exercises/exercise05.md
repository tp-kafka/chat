# Kafka Streams Unit Testing Exercise in Spring Boot

## Goals

- Learn to write unit tests for Kafka Streams in a Spring Boot application.
- Acquire skills to debug Kafka topologies.

## Preparation

- Follow the instructions in `README.md` to set up your IDE for the exercise.

## Tasks for Attendees

### Working in `KafkaTopologyTest`

- Switch to the `tp.kafka.chat.core.KafkaTopologyTest` class.
- Implement various testing methods focusing on Kafka Streams:

#### Implement `messages_shouldBeTransmitted`

- Create a test case to ensure messages are correctly transmitted through the topology.

#### Implement `messages_shouldBeFiltered_IfContainingABadWord`

- Write a test to verify that messages containing a "bad word" are appropriately filtered.

#### Implement `messages_shouldBeFiltered_IfUserIsTimedOut`

- Develop a test to check if messages are filtered when the user is timed out.

### Debugging Topology

- Try debugging your topology to understand the flow and identify potential issues.

### Understanding Kafka Streams Testing Mechanics

- Investigate how input and output topics are created for the tests.
- Explore how it is ensured that messages from different tests do not influence each other.

### Reflecting on Testing Practices

- Discuss whether these unit tests can replace integration tests or if both are necessary for comprehensive testing.

## Conclusion

This exercise aims to deepen your understanding of unit testing and debugging in the context of Kafka Streams within a Spring Boot application, essential skills for developing robust and fault-tolerant streaming applications.

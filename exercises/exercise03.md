# Kafka Streams Training Exercise in Spring Boot

## Goals
- Learn to utilize Kafka Streams within a Spring Boot application.
- Understand the concept of stream-table duality in Kafka.

## Preparation
- Set up your IDE as per instructions in `README.md`.

## Starting Point
- Access the `tp.kafka.chat.api.BadWordsHttpApi` class, containing endpoints to manage words in the `badwords` topic.

## Tasks for Attendees
### Understanding `BadWordsHttpApi`
- Review `tp.kafka.chat.api.BadWordsHttpApi`.
  - Note how it's similar to previously implemented APIs.

### Observing Kafka Events
- Interact with the `badwords` topic by:
  - Writing one or more words via the api.
  - Deleting one or more words via the api.
- Observe the events in Kafka during these operations.

### Implementing Global Table in `KafkaTopology`
- In `tp.kafka.chat.core.KafkaTopology`, focus on the `badWordGlobalTable` method.
- Create a consumer configuration:
  - Utilize the `consumend` class.
  - Apply serdes (serializer/deserializer) provided in the class.
  - Assign a consumer name, e.g., "badword-source".
- Construct a global table:
  - Use the specified topic, your consumer configuration, and the given materialization parameters.
- what happens with the state when you restart you spring-boot app?
- what happens when you do not define a global (but a normal) table?

### Verifying the Table Contents
- Use the read endpoint to inspect the contents of your table.

This exercise will give attendees a practical understanding of handling data streams and global tables in Kafka within a Spring Boot environment, enhancing their ability to manage real-time data flows.

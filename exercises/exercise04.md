# Kafka Streams Training Exercise: Joining and Filtering in Spring Boot

## Goals
- Learn to use Kafka Streams in a Spring Boot application.
- Understand how to join and filter tables and streams.

## Preparation
- Set up your IDE following the instructions in `README.md`.

## Tasks for Attendees
### Working with `KafkaTopology`
- Navigate to `tp.kafka.chat.core.KafkaTopology` and focus on the `modMessageStream` method.

### Implementing Stream Modification
1. **Utilize `messageSourceStream`**:
   - Begin with the `messageSourceStream`.

2. **Key Selection**:
   - Select the login of the sender as the new key.

3. **Repartitioning**:
   - Repartition the stream using `stringSerde` and `messageSerde`.
   - This step should yield a stream keyed by the login.

4. **Left-Join with `timeoutTable`**:
   - Perform a left-join of this stream on `timeoutTable`.
   - Use `this::joiner` for the join and provide a `Joined` definition with the three serdes.
   - This should result in a stream with messages containing an additional `mod_only` attribute.

5. **Repartition Back to Channel-Name**:
   - Repartition the stream back to channel-name.

### Filtering in `modMessageStream`
- In the `modMessageStream` method:
  - Apply a filter so that only messages with `mod_only == false` are retained.

This exercise will provide practical insights into advanced Kafka Streams operations like joining and filtering, enhancing your ability to manage complex data flows in a Spring Boot application.

# Kafka Connect Training Exercise

## Goals

- Learn to utilize Kafka Connect for integrating various applications.

## Preparation

- Follow instructions in `README.md` to set up your IDE.

## Resources Provided

- A running Kafka Connect instance with an installed IRC connector.
- Kafka UI, configured for use with Kafka Connect.

## Tasks for Attendees

### Exploring Integration Options

- Browse [Confluent Hub](https://www.confluent.io/hub/) to understand the range of integrable applications.

### Understanding the IRC Connector

- Review the [Kafka Connect IRC plugin](https://github.com/cjmatta/kafka-connect-irc) we'll use in this exercise.

### Choosing an IRC Channel

- Select an IRC channel for importing messages into our application.
  - Refer to the last section for assistance if needed.

### Configuring Kafka Connect

- Use Kafka UI to configure Kafka Connect for reading messages from your chosen IRC channel.
- Set the following properties in the configuration:

    ```yaml
    "connector.class": "com.github.cjmatta.kafka.connect.irc.IrcSourceConnector",
    
    "key.converter.schemas.enable": false,
    "value.converter.schemas.enable": true,
    "value.converter": "io.confluent.connect.protobuf.ProtobufConverter",
    "value.converter.schema.registry.url": "http://localhost:8085",
    
    "irc.channels": "{{ CHANNEL (LIST) HERE }}",
    "irc.server": "{{ IRC SERVER HERE }}",
    "irc.bot.name": "{{ YOUR USERNAME HERE }}",
    "kafka.topic": "chat",
    "tasks.max": "1"
    ```

- Replace placeholders (`{{ }}`) with appropriate values for the IRC channel, server, and your username.

- Review the incoming messages in the topic view.

This exercise will provide hands-on experience with setting up and configuring Kafka Connect for real-time data integration from an IRC channel.

## WTF is IRC

IRC, or Internet Relay Chat, is a protocol for real-time Internet text messaging. It's an earlier version of modern chat platforms like Discord. It facilitates group (channel) communications as well as private messaging. Widely used for community discussions, technical support, and collaborative work.

If you don't have a favorite IRC channel, consider using an online Twitch channel.

### Configuration for Using a Twitch Channel
To connect to a Twitch channel, use the following settings:
{{ IRC SERVER HERE }}: irc.chat.twitch.tv
{{ YOUR USERNAME HERE }}: justinfan + random number (for anonymous access)
{{ CHANNEL (LIST) HERE }}: the channels you chose (including the hash as comma separated list)

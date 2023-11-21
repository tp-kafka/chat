# Chat Application

Welcome to the Chat Application, a part of our Kafka training program! This application is designed to provide you with hands-on exercises and examples for working with [Kafka Streams](https://kafka.apache.org/documentation/streams/) in a [Spring Boot](https://spring.io/projects/spring-boot) environment.

## Overview

Kafka Streams is a powerful library for building real-time stream processing applications using Apache Kafka. In this training module, we have created the Chat application to demonstrate various concepts and techniques for stream processing with Kafka Streams.

## Features

* Real-time Chat: Explore a real-time application that leverages Kafka Streams to process messages instantly.
* Interactive Exercises: We provide a series of interactive exercises to help you understand Kafka Streams concepts and how they apply in a practical scenario.
* Example Code: Alongside the exercises, you'll find example code snippets and complete applications to illustrate key concepts and best practices.
* Spring Boot Integration: Learn how to integrate and test Kafka Streams seamlessly with Spring Boot, a popular framework for building Java applications.
* Hands-on Learning: Get hands-on experience in setting up Kafka, configuring Kafka Streams, and building real-time applications.

## Getting Started

To get started with the Chat application and the Kafka training exercises, please follow the instructions in the following chapters.

### IDE Setup

To make it easy to get started with the Chat application and the Kafka training exercises, we recommend using GitHub Codespaces, which provides a fully configured development environment right in your browser. With GitHub Codespaces, you won't need to worry about setting up your local development environment or installing any dependencies.

Follow these steps to start a GitHub Codespace with this repository:

1. Login to GitHub: Make sure you're logged in to your GitHub account.
2. Open the Repository: Navigate to the Chat application repository on GitHub that you want to work on.
3. Click on "Code": In the upper-right corner of the repository page, click on the "Code" button.
4. Select "Open with Codespaces": From the dropdown menu, select "Open with Codespaces."
5. Wait for Codespace to Launch: GitHub will set up a Codespace for you with all the necessary tools and configurations. This may take a few moments.
6. In the explorer pane choose "Java Projects" and import the project.
7. Once the Codespace is ready, you can access it right in your browser. You'll have a fully functional development environment, including a terminal, code editor, and all the dependencies required for the Chat application.

That's it! You're now ready to start working on the Chat application and the Kafka training exercises directly from your GitHub Codespace. No additional setup is required, and you can focus on learning and coding without the hassle of configuring your local environment.

Happy coding!

### Usage

#### Open Kafka UI

The [Kafka UI](https://github.com/provectus/kafka-ui) is already running in your Codespace. To access it, follow these steps:

1. In your GitHub Codespace, open the "Ports" view by clicking the tab.
2. Look for labeled 'Kafka UI (8080)' and click on the 'Open in Browser' symbol, that appears in the column 'Forwarded Address' when you hover the line

A new tab should open in your browser. You can now explore Kafka topics, messages, and other Kafka-related information using the Kafka UI.

#### Start Chat Application

To start the Chat Application, you can use the Spring Boot Dashboard in your IDE. Follow these steps:

1. Locate the Spring Boot Dashboard in your IDE's toolbar.
2. In the Spring Boot Dashboard, you should see the Chat Application listed as a project. Click on it.
3. Click the "Run" button to launch the Chat Application.

##### Open Swagger UI

[Swagger UI](https://swagger.io/tools/swagger-ui/) is mapped to the root path of the application. To access it, follow these steps:

1. In your GitHub Codespace, open the "Ports" view by clicking the tab.
2. Look for labeled 'Spring Boot App (9000)' and click on the 'Open in Browser' symbol, that appears in the column 'Forwarded Address' when you hover the line

A new tab should open in your browser. You can use all available http endpoints from here.

## Exercises

Dive into the exercises folder to enhance your understanding of Kafka Streams. These exercises are designed to be hands-on and provide you with practical experience in building real-time stream processing applications.

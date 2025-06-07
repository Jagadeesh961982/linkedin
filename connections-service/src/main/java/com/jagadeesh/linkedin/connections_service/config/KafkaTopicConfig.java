package com.jagadeesh.linkedin.connections_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic connectionRequestSentTopic() {
        return new NewTopic("connection-request-sent-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic connectionRequestAcceptedTopic() {
        return new NewTopic("connection-request-accepted-topic", 3, (short) 1);
    }

    // @Bean
    // public NewTopic connectionRequestRejectedTopic() {
    //     return new NewTopic("connection-request-rejected-topic", 3, (short) 1);
    // }

    // @Bean
    // public NewTopic connectionDeletedTopic() {
    //     return new NewTopic("connection-deleted-topic", 3, (short) 1);
    // }
}

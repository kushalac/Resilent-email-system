package com.example.demo;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic signupTopic() {
        return createNewTopic("signup-topic");
    }

    @Bean
    public NewTopic promotionsTopic() {   //add validity or set time period
        return createNewTopic("promotions-topic");
    }

    @Bean
    public NewTopic latestPlansTopic() {
        return createNewTopic("latest-plans-topic");
    }

    @Bean
    public NewTopic releaseEventsTopic() {
        return createNewTopic("release-events-topic");
    }
    
    @Bean
    public NewTopic accountDeletedTopic() {
        return createNewTopic("account-deleted-topic");
    }

    @Bean
    public NewTopic accountModifiedTopic() {
        return createNewTopic("account-modified-topic");
    }
    
    private NewTopic createNewTopic(String topicName) {
        Properties adminConfig = new Properties();
        adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminConfig)) {
            // Check if the topic already exists
            if (!adminClient.listTopics().names().get().contains(topicName)) {
                // If the topic doesn't exist, create it
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                return newTopic;
            } else {
                // Topic already exists, print a message
                System.out.println("Topic '" + topicName + "' already exists.");
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions here
            e.printStackTrace();
            throw new RuntimeException("Failed to create or check topic: " + topicName, e);
        }
    }
}
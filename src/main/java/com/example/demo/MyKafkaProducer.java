package com.example.demo;

import org.apache.kafka.clients.producer.*;
import org.springframework.stereotype.Component;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;


@Component
public class MyKafkaProducer {


    public CompletableFuture<Void> sendMessage(String name, String email, String topic, String message, String subject) {
        return CompletableFuture.runAsync(() -> {
            // Create a Kafka producer configuration
            Properties producerProps = new Properties();
            producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

            // Create a Kafka producer instance
            Producer<String, String> producer = new KafkaProducer<>(producerProps);

            try {
               

                // Create a Kafka producer record
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);

                // Send the message to the Kafka topic
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        if (exception == null) {
                            System.out.println("Message sent successfully to topic: " + metadata.topic());
                            
                            // After sending the Kafka message, send an email
                           
                           
                            EmailSender.sendEmail(email, subject, message);
                        } else {
                            System.err.println("Error sending message: " + exception.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producer.close();
            }
        });
    }


}

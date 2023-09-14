package com.example.demo;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.user.MyUserConsumer;

import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class MyKafkaProducer {
    
	private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MyKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<Void> sendMessage(String topic, String email) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, email);

        return CompletableFuture.supplyAsync(() -> {
            try {
                SendResult<String, String> sendResult = kafkaTemplate.send(record).get();
                System.out.println("Message sent successfully to topic: " + sendResult.getRecordMetadata().topic());
                return null;
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Error sending message: " + ex.getMessage());
                throw new RuntimeException(ex); // Propagate the exception
            }
        });
    }
    
    public CompletableFuture<Void> sendNotificationMessage(String topic, String info) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic,info);

        return CompletableFuture.supplyAsync(() -> {
            try {
                SendResult<String,String> sendResult = kafkaTemplate.send(record).get();
                System.out.println("Message sent successfully to topic: " + sendResult.getRecordMetadata().topic());
                return null;
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println("Error sending message: " + ex.getMessage());
                throw new RuntimeException(ex); // Propagate the exception
            }
        });
    }

}

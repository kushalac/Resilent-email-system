package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.repo.NotificationRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.user.User;

@Component
public class MyNotificationConsumer {
	
	@Autowired
	UserRepository userRepoCall;
	
	@Autowired
    NotificationRepository NotificationRepoCall;
    
    @Autowired
    private NotificationEmailGenerator notificationEmailGenerator;
    
    @KafkaListener(topics = "promotions-topic")
    public void sendPromotionEmail(String info) {
        System.out.println("Received in promotion consumer");
        String delimiter = "%";
        String[] parts = info.split(delimiter);

        if (parts.length >= 2) {
            String email = parts[0];
            String id = parts[1];
            Optional<Notification> optionalNotification = NotificationRepoCall.findById(id);
            User user = userRepoCall.findByEmail(email);
            String name = user.getName();
            if (optionalNotification.isPresent()) {
                Notification notification = optionalNotification.get();
                String subject = notification.getNotificationSubject();
                String content = notification.getNotificationContent();
                String emailContent = notificationEmailGenerator.generateNotificationEmailContent(name, content);
                EmailSender.sendEmail(email, subject, emailContent);
            }
        } else {
            // Handle the case where the message format is incorrect.
            // You can log an error or take appropriate action.
            System.err.println("Invalid message format received: " + info);
        }
    }
    
    @KafkaListener(topics = "latestPlans-topic")
    public void sendLatestPlansEmail(String info) {
        System.out.println("Received in latest-plans consumer");
        String delimiter = "%";
        String[] parts = info.split(delimiter);

        if (parts.length >= 2) {
            String email = parts[0];
            String id = parts[1];
            Optional<Notification> optionalNotification = NotificationRepoCall.findById(id);
            User user = userRepoCall.findByEmail(email);
            String name = user.getName();
            if (optionalNotification.isPresent()) {
                Notification notification = optionalNotification.get();
                String subject = notification.getNotificationSubject();
                String content = notification.getNotificationContent();
                String emailContent = notificationEmailGenerator.generateNotificationEmailContent(name, content);
                EmailSender.sendEmail(email, subject, emailContent);
            }
        } else {
            // Handle the case where the message format is incorrect.
            // You can log an error or take appropriate action.
            System.err.println("Invalid message format received: " + info);
        }
    }
    
    @KafkaListener(topics = "releaseEvents-topic")
    public void sendRelaeseEventEmail(String info) {
        System.out.println("Received in release-events consumer");
        String delimiter = "%";
        String[] parts = info.split(delimiter);

        if (parts.length >= 2) {
            String email = parts[0];
            String id = parts[1];
            Optional<Notification> optionalNotification = NotificationRepoCall.findById(id);
            User user = userRepoCall.findByEmail(email);
            String name = user.getName();
            if (optionalNotification.isPresent()) {
                Notification notification = optionalNotification.get();
                String subject = notification.getNotificationSubject();
                String content = notification.getNotificationContent();
                String emailContent = notificationEmailGenerator.generateNotificationEmailContent(name, content);
                EmailSender.sendEmail(email, subject, emailContent);
            }
        } else {
            // Handle the case where the message format is incorrect.
            // You can log an error or take appropriate action.
            System.err.println("Invalid message format received: " + info);
        }
    }
}
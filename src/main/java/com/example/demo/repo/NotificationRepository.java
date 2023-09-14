package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    boolean existsByNotificationId(String notificationId);
    boolean existsByNotificationTypeAndNotificationSubject(String notificationType, String notificationSubject);
    Notification findByNotificationTypeAndNotificationSubject(String notificationType, String notificationSubject);
    
    // Add this method to find a notification by ID
    //Notification findById(String notificationId);
}

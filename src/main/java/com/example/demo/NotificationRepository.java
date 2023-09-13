package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	boolean existsByNotificationId(String notificationId);
	boolean existsByNotificationTypeAndNotificationSubject(String notificationType, String notificationSubject);
}
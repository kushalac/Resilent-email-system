package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.user.User;
import com.example.demo.repo.NotificationRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MyKafkaProducer kafkaproducer;
    
    @Autowired
    private  NotificationRepository notificationRepoCall;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    
    @PostMapping("/createNotification")
    public ResponseEntity<String> createNewNotification(@RequestBody Notification notification) {
        Notification existingNotification = notificationRepoCall.findByNotificationTypeAndNotificationSubject(
                notification.getNotificationType(), notification.getNotificationSubject());

        if (existingNotification != null) {
            if (existingNotification.isActiveNotification()) {
                // If an active notification with the same type and subject exists, return a response indicating that
                return ResponseEntity.badRequest().body("Notification with type '" + notification.getNotificationType() +
                        "' and subject '" + notification.getNotificationSubject() + "' already exists.");
            } else {
                // If an inactive notification with the same type and subject exists, update it
                existingNotification.setNotificationContent(notification.getNotificationContent());
                existingNotification.setActiveNotification(true);
                notificationRepoCall.save(existingNotification);
                return ResponseEntity.ok("Notification created successfully.");
            }
        }

        // Save the notification (MongoDB will generate the _id)
        notificationRepoCall.save(notification);
        return ResponseEntity.ok("Notification created and saved successfully.");
    }

    
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, String> requestMap) {
        String notificationType = requestMap.get("notificationType");
        String notificationSubject = requestMap.get("notificationSubject");
        Notification notification = notificationRepoCall.findByNotificationTypeAndNotificationSubject(notificationType, notificationSubject);

        if (notification == null || !notification.isActiveNotification()) {
            return ResponseEntity.badRequest().body("Notification not found");
        }

        // Send the notification email
        sendNotificationEmail(notification.getNotificationType(), notification.getNotificationSubject(), notification.getNotificationContent(), notification);

        return ResponseEntity.ok("Notification sent successfully.");
    }
     
    @PutMapping("/updateNotification")
    public ResponseEntity<String> updateNotification(@RequestBody Notification updatedNotification) {
        // Check if a notification with the same type and subject exists
        Notification existingNotification = notificationRepoCall.findByNotificationTypeAndNotificationSubject(
                updatedNotification.getNotificationType(), updatedNotification.getNotificationSubject());

        if (existingNotification == null) {
        	return ResponseEntity.ok("Notification with type "+updatedNotification.getNotificationType()+ " and with subject "+updatedNotification.getNotificationSubject() +" doent exist.");
        }

        try {
            // Update the existing notification with the new content
            existingNotification.setNotificationContent(updatedNotification.getNotificationContent());
            // You can update other fields as needed

            // Save the updated notification
            notificationRepoCall.save(existingNotification);

            return ResponseEntity.ok("Notification updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating notification: " + e.getMessage());
        }
    }
    
    @GetMapping("/getNotificationSubjects")
    public ResponseEntity<List<String>> getNotificationSubjects(@RequestParam String notificationType) {
        List<Notification> allNotifications = notificationRepoCall.findAll();
        List<String> subjects = new ArrayList<>();
        
        for (Notification notification : allNotifications) {
            if (notification.getNotificationType().equals(notificationType) && notification.isActiveNotification()) {
                subjects.add(notification.getNotificationSubject());
            }
        }
        
        // Always return a response with the subjects, even if it's an empty list
        return ResponseEntity.ok(subjects);
    }



    @GetMapping("/getNotificationContent")
    public ResponseEntity<String> getNotificationContent(@RequestParam String notificationType, @RequestParam String notificationSubject) {
        Notification notification = notificationRepoCall.findByNotificationTypeAndNotificationSubject(notificationType, notificationSubject);

        if (notification != null && notification.isActiveNotification()) {
            return ResponseEntity.ok(notification.getNotificationContent());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteNotification")
    public ResponseEntity<String> deleteNotification(
            @RequestParam String notificationType,
            @RequestParam String notificationSubject) {
        // Find the notification to delete based on type and subject
        Notification notificationToDelete = notificationRepoCall.findByNotificationTypeAndNotificationSubject(
                notificationType, notificationSubject);

        if (notificationToDelete == null) {
            return ResponseEntity.ok("Notification not found.");
        }

        try {
            // Set activeNotification to false instead of deleting
            notificationToDelete.setActiveNotification(false);
            notificationRepoCall.save(notificationToDelete);
            return ResponseEntity.ok("Notification deactivated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deactivating notification: " + e.getMessage());
        }
    }

      

private void sendNotificationEmail(String notificationType, String subject, String message, Notification notification) {
    Query query = new Query();
    query.addCriteria(Criteria.where("receiveNotifications").is(true)
                             .and("notifications." + notificationType).is(true)
                             .and("active").is(true)
                             .and("id").nin(notification.getUserList()));
    List<User> eligibleUsers = mongoTemplate.find(query, User.class);
    System.out.println(eligibleUsers);
    String id = notification.getNotificationId();
    for (User user : eligibleUsers) {
    
         long timestamp = System.currentTimeMillis();
      // Convert the timestamp to a human-readable date and time format
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String formattedTimestamp = sdf.format(new Date(timestamp));
         user.logReceivedNotification(id, formattedTimestamp);
         userRepository.save(user); // Save the updated user entity

        String email = user.getEmail();
        String topic = notificationType+ "-topic";
        String info = email+"%"+id;
        kafkaproducer.sendMessage(topic,info);
        notification.getUserList().add(user.getId());
    }

    
    notificationRepoCall.save(notification);
}

}
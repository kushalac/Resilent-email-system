package com.example.demo;

import com.example.demo.User;
import com.example.demo.UserRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private MyKafkaProducer kafkaproducer;
    
    @Autowired
    private  NotificationRepository notificationRepoCall;
    
    @Autowired
    private MongoTemplate mongoTemplate;

	/*
	 * @GetMapping("/promotions")
	 * 
	 * @ResponseBody public String sendPromotionsEmail() {
	 * sendNotificationEmail("promotions","Promotion subject", "promotions code 1");
	 * return "Promotions emails sent!"; }
	 * 
	 * @GetMapping("/latestplans")
	 * 
	 * @ResponseBody public String sendLatestPlansEmail() {
	 * sendNotificationEmail("latestPlans","LatestPlan subject",
	 * "Check out our latest plans!"); return "Latest plans emails sent!"; }
	 * 
	 * @GetMapping("/releaseevents")
	 * 
	 * @ResponseBody public String sendReleaseEventsEmail() {
	 * sendNotificationEmail("releaseEvents","release eveny subject",
	 * "New release events are here!"); return "Release events emails sent!"; }
	 */
    
    
    @PostMapping("/NewNotification")
    public void createNewNotification(@RequestBody Notification notification) {
			String id=notification.getNotificationId();
    		String topic=notification.getNotificationType();
			String subject= notification.getNotificationSubject();
			String message= notification.getNotificationContent();
			
			if(notificationRepoCall.existsByNotificationId(id))
			{
				Notification existnotification = notificationRepoCall.findById(id).orElse(null);
				sendNotificationEmail(topic,subject,message,existnotification);
			}
			
			else
			{
				notificationRepoCall.save(notification);
				sendNotificationEmail(topic,subject,message,notification);
				
			}
    }

      
    @PatchMapping("/updateuser")
    @ResponseBody
    public ResponseEntity<String> updateUserPreferences(@RequestBody Map<String, String> requestMap) {
        try {
            String email = requestMap.get("email");
            boolean promotions = Boolean.parseBoolean(requestMap.get("promotions"));
            boolean latestPlans = Boolean.parseBoolean(requestMap.get("latest plans"));
            boolean releaseEvents = Boolean.parseBoolean(requestMap.get("release events"));

            User existingUser = userRepository.findByEmail(email);

            if (existingUser != null) {
                existingUser.getNotifications().put("promotions", promotions);
                existingUser.getNotifications().put("latest plans", latestPlans);
                existingUser.getNotifications().put("release events", releaseEvents);

                userRepository.save(existingUser);

                return ResponseEntity.ok("User preferences updated successfully");
            } else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    private void sendNotificationEmail(String notificationType, String subject, String message, Notification notification) {
        Query query = new Query();
        query.addCriteria(Criteria.where("receiveNotifications").is(true)
                                 .and("notifications." + notificationType).is(true)
                                 .and("id").nin(notification.getUserList()));

        List<User> eligibleUsers = mongoTemplate.find(query, User.class);
        //System.out.println(eligibleUsers);
        for (User user : eligibleUsers) {
            String name = user.getName();
            String email = user.getEmail();
            String topic = notificationType.replaceAll("\\s+", "-").toLowerCase() + "-topic";
            kafkaproducer.sendMessage(name, email, topic, message, subject);
            kafkaTemplate.send(topic, message);
            notification.getUserList().add(user.getId());
        }

        
        notificationRepoCall.save(notification);
    }

    }
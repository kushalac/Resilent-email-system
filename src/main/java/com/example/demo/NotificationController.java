package com.example.demo;

import com.example.demo.User;
import com.example.demo.UserRepository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private MyKafkaProducer kafkaproducer;

    @GetMapping("/promotions")
    @ResponseBody
    public String sendPromotionsEmail() {
        sendNotificationEmail("promotions","Promotion subject", "promotions code 1");
        return "Promotions emails sent!";
    }

    @GetMapping("/latestplans")
    @ResponseBody
    public String sendLatestPlansEmail() {
        sendNotificationEmail("latest plans","LatestPlan subject", "Check out our latest plans!");
        return "Latest plans emails sent!";
    }

    @GetMapping("/releaseevents")
    @ResponseBody
    public String sendReleaseEventsEmail() {
        sendNotificationEmail("release events","release eveny subject", "New release events are here!");
        return "Release events emails sent!";
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

    private void sendNotificationEmail(String notificationType,String subject, String message) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.isReceiveNotifications() && user.getNotifications().containsKey(notificationType)
                    && user.getNotifications().get(notificationType)) {
            	String name=user.getName();
                String email = user.getEmail();
                String topic = notificationType.replaceAll("\\s+", "-").toLowerCase() + "-topic";
                kafkaproducer.sendMessage(name,email,topic,message,subject);
                kafkaTemplate.send(topic, message);
            }
        }
    }
}

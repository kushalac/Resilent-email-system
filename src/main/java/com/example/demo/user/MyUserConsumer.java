package com.example.demo.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.demo.EmailSender;
import com.example.demo.repo.UserRepository;

@Component
public class MyUserConsumer {

    @Autowired
    UserRepository userRepoCall;
    
    @Autowired
    private UserEmailGenerator userEmailGenerator;
    
    @KafkaListener(topics = "signup-topic")
    public void sendSignUpEmail(String email){
        
    	System.out.println("Received in signup consumer");
        User user = userRepoCall.findByEmail(email);
        if (user != null) {
            String subject = "Welcome to our service";
            int eventCode=0;
            String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
            EmailSender.sendEmail(user.getEmail(), subject, emailContent);
        } else {
            // Handle the case when the user is not found
            System.out.println("User not found for email: " + email);
        }
    }
    
    
    @KafkaListener(topics = "account-modified-topic")
    public void sendModifiedEmail(String email){
        
    	System.out.println("Received in modify consumer");
        User user = userRepoCall.findByEmail(email);
        if (user != null) {
            String subject = "Account Updated";
            int eventCode=1;
            String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
            EmailSender.sendEmail(user.getEmail(), subject, emailContent);
        } else {
            // Handle the case when the user is not found
            System.out.println("User not found for email: " + email);
        }
    }
    
    @KafkaListener(topics = "account-deleted-topic")
    public void sendDeletedEmail(String email){
        
    	System.out.println("Received in delete consumer");
        User user = userRepoCall.findByEmail(email);
        if (user != null) {
            String subject = "Account Deleted";
            int eventCode=2;
            userRepoCall.delete(user);
            String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
            EmailSender.sendEmail(user.getEmail(), subject, emailContent);
        } else {
           
            System.out.println("User not found for email: " + email);
        }
    }

    
    @KafkaListener(topics = "deleteConfirmation-topic")
    public void sendConfirmationbeforeDeleting(String info){
        
    	System.out.println("Received in confirm-delete consumer");
        String delimiter = "%";
        String[] parts = info.split(delimiter);
        
        if (parts.length >= 2) {
            String email = parts[0];
            String content = parts[1];
            String subject="Confirm Before Deletion";
            User user = userRepoCall.findByEmail(email);
        if (user != null) {
            
            String emailContent= userEmailGenerator.generateUserEmailContentDeletion(user,content);
            EmailSender.sendEmail(email, subject, emailContent);
        } else {
           
            System.out.println("User not found for email: " + email);
        }
        }
    }
    
}
package com.example.demo.user;
import java.io.IOException;
import java.util.List;

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
        String subject = "Sign-up Success";
        int eventCode=0;
        String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
        EmailSender.sendEmail(user.getEmail(), subject, emailContent);
    }
    
    
    @KafkaListener(topics = "account-modified-topic")
    public void sendModifiedEmail(String email){
        
    	System.out.println("Received in modify consumer");
        User user = userRepoCall.findByEmail(email);
        String subject = "Account Updation";
        int eventCode=1;
        String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
        EmailSender.sendEmail(user.getEmail(), subject, emailContent);
    }
    
    @KafkaListener(topics = "account-deleted-topic")
    public void sendDeletedEmail(String email){
        
    	System.out.println("Received in delete consumer");
        User user = userRepoCall.findByEmail(email);
        String subject = "Account Deletion";
        int eventCode=2;
        String emailContent= userEmailGenerator.generateUserEmailContent(user,eventCode);
        EmailSender.sendEmail(user.getEmail(), subject, emailContent);
    }
}

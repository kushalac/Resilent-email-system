package com.example.demo.user;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEmailGenerator {

    @Autowired
    private Configuration freemarkerConfig;

    public String generateUserEmailContent(User user, int eventCode) {
        try {
            String templatename = "";
            String status="";

            if (eventCode == 0)
                templatename = "signup_template.ftl";
            else if (eventCode == 1) {
                templatename = "modify_template.ftl";
                Boolean receiveNotification = user.isReceiveNotifications();
                status = "You will receive notifications for: ";

                if (!receiveNotification) {
                    status = "You won't receive any notifications";
                } else {
                    Map<String, Boolean> userNotifications = user.getNotifications();
                    
                    if (userNotifications.get("promotions") != null && userNotifications.get("promotions")) {
                        status += "promotions, ";
                    }
                    if (userNotifications.get("latestPlans") != null && userNotifications.get("latestPlans")) {
                        status += "latest plans, ";
                    }
                    if (userNotifications.get("releaseEvents") != null && userNotifications.get("releaseEvents")) {
                        status += "release events, ";
                    }

                    // Remove the trailing comma and space
                    status = status.substring(0, status.length() - 2);
                }
                
            }
            else if (eventCode == 2)
                templatename = "delete_template.ftl";

            Template template = freemarkerConfig.getTemplate(templatename);

            String name = user.getName();
            Map<String, Object> model = new HashMap<>();
            model.put("status", status);
            model.put("name", name);

            StringWriter emailContentWriter = new StringWriter();
            template.process(model, emailContentWriter);

            return emailContentWriter.toString();
        } catch (IOException | TemplateException e) {
            System.out.println("Error generating email content: " + e.getMessage());
            return "Error: Unable to generate email content";
        }
    }
    
    public String generateUserEmailContentDeletion(User user, String content) {
        try {
            String templatename = "confirmDeletion_template.ftl";
            Template template = freemarkerConfig.getTemplate(templatename);
            String name = user.getName();
           
            Map<String, Object> model = new HashMap<>();
            model.put("name", name);
            model.put("content",content);

            StringWriter emailContentWriter = new StringWriter();
            template.process(model, emailContentWriter);

            return emailContentWriter.toString();
        } catch (IOException | TemplateException e) {
            System.out.println("Error generating email content: " + e.getMessage());
            return "Error: Unable to generate email content";
        }
    }
}

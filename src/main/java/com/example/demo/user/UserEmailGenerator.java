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

            if (eventCode == 0)
                templatename = "signup_template.ftl";
            else if (eventCode == 1)
                templatename = "modify_template.ftl";
            else if (eventCode == 2)
                templatename = "delete_template.ftl";

            Template template = freemarkerConfig.getTemplate(templatename);

            String name = user.getName();
            String confirmationLink = "https://www.google.com";
            Map<String, Object> model = new HashMap<>();
            model.put("name", name);
            model.put("confirmationLink",confirmationLink);

            StringWriter emailContentWriter = new StringWriter();
            template.process(model, emailContentWriter);

            return emailContentWriter.toString();
        } catch (IOException | TemplateException e) {
            System.out.println("Error generating email content: " + e.getMessage());
            return "Error: Unable to generate email content";
        }
    }
}

package com.example.demo;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class NotificationEmailGenerator {
	
	@Autowired
    private Configuration freemarkerConfig;

    public String generateNotificationEmailContent(String name, String content) {
        try {
            String templatename = "demo.ftl";

            Template template = freemarkerConfig.getTemplate(templatename);

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

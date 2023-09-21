package com.example.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "com.example.demo.user", "com.example.demo.repo"})
public class EmailNotificationSystemNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailNotificationSystemNewApplication.class, args);
	}

}

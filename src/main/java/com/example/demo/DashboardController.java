package com.example.demo;

import com.example.demo.repo.UserRepository;
import com.example.demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getStats")
    public DashboardStats getDashboardStats() {
        List<User> allUsers = userRepository.findAll();

        long activeTrueCount = allUsers.stream().filter(User::isActive).count();
        long activeFalseCount = allUsers.stream().filter(user -> !user.isActive()).count();
        long receiveNotificationsCount = allUsers.stream()
                .filter(user -> user.isActive() && user.isReceiveNotifications())
                .count();
        long promotionsCount = allUsers.stream()
                .filter(user -> user.isReceiveNotifications() &&  user.isActive() && Boolean.TRUE.equals(user.getNotifications().get("promotions")))
                .count();
        long latestPlansCount = allUsers.stream()
                .filter(user -> user.isReceiveNotifications() &&  user.isActive() && Boolean.TRUE.equals(user.getNotifications().get("latestPlans")))
                .count();
        long releaseEventsCount = allUsers.stream()
                .filter(user -> user.isReceiveNotifications()&&  user.isActive() && Boolean.TRUE.equals(user.getNotifications().get("releaseEvents")))
                .count();
        long notReceiveNotificationsCount = allUsers.stream().filter(user -> user.isActive() && !user.isReceiveNotifications()).count();

        return new DashboardStats(activeTrueCount, activeFalseCount, receiveNotificationsCount,
                promotionsCount, latestPlansCount, releaseEventsCount, notReceiveNotificationsCount);
    }
    @PostMapping("/getUsersByNotifications")
    public List<User> getUsersByNotifications(@RequestBody List<String> selectedNotifications) {
        // Filter users based on selectedNotifications
        return userRepository.findAll().stream()
                .filter(user -> user.isActive() && user.isReceiveNotifications() &&
                        selectedNotifications.stream().anyMatch(notification ->
                                Boolean.TRUE.equals(user.getNotifications().get(notification))))
                .collect(Collectors.toList());
    }


}

package org.example.controller;


import org.example.model.Notification;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    // Simulated notification data
    private static final List<Notification> MOCK_NOTIFICATIONS = List.of(
            new Notification("1", "sam123", "Order #12406 has been confirmed."),
            new Notification("2", "sam123", "Shipment for Order #12406 has been delivered."),
            new Notification("3", "john321", "Welcome to Farm2Table, John!")
    );

    // GET /notifications/{userId}
    @GetMapping("/{userId}")
    public List<Notification> getNotificationsForUser(@PathVariable String userId) {
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification n : MOCK_NOTIFICATIONS) {
            if (n.getUserId().equals(userId)) {
                userNotifications.add(n);
            }
        }
        return userNotifications;
    }
}

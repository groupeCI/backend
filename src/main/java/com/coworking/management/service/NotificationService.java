package com.coworking.management.service;

import com.coworking.management.entity.User;
import com.coworking.management.entity.Notification;
import com.coworking.management.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private EmailService emailService;

    public void createNotification(User user, String title, String message, boolean sendEmail) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);
        notificationRepository.save(notification);
        
        if (sendEmail) {
            emailService.sendConfirmationEmail(
                user.getEmail(), 
                title, 
                message
            );
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
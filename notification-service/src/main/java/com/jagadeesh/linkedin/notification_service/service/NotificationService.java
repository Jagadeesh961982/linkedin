package com.jagadeesh.linkedin.notification_service.service;

import org.springframework.stereotype.Service;

import com.jagadeesh.linkedin.notification_service.entity.Notification;
import com.jagadeesh.linkedin.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    

    public void sendNotification(Long userId, String message){
        Notification notification=new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);

        notificationRepository.save(notification);
    }
}

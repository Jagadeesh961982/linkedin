package com.jagadeesh.linkedin.notification_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.jagadeesh.linkedin.connections_service.event.ConnectionRequestAcceptedEvent;
import com.jagadeesh.linkedin.connections_service.event.ConnectionRequestSentEvent;
import com.jagadeesh.linkedin.notification_service.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "connection-request-sent-topic")
    public void handleConnectionRequestSentEvent(ConnectionRequestSentEvent event) {
        log.info("Received ConnectionRequestSentEvent: {}", event);
        Long receiverId = event.getReceiverId();
        Long senderId = event.getSenderId();
        String message = String.format("You have received a connection request from user %d", senderId);

        notificationService.sendNotification(receiverId, message);
        log.info("Notification sent to user {}: {}", receiverId, message);

    }

    @KafkaListener(topics = "connection-request-accepted-topic")
    public void handleConnectionRequestAcceptedEvent(ConnectionRequestAcceptedEvent event) {
        log.info("Received ConnectionRequestAcceptedEvent: {}", event);
        Long receiverId = event.getReceiverId();
        Long senderId = event.getSenderId();
        String message = String.format("Your connection request to user %d has been accepte by", receiverId);

        notificationService.sendNotification(senderId, message);
        log.info("Notification sent to user {}: {}", receiverId, message);
    }
}

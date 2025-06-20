package com.jagadeesh.linkedin.notification_service.consumer;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.jagadeesh.linkedin.notification_service.clients.ConnectionsClient;
import com.jagadeesh.linkedin.notification_service.dto.PersonDto;
import com.jagadeesh.linkedin.notification_service.entity.Notification;
import com.jagadeesh.linkedin.notification_service.repository.NotificationRepository;
import com.jagadeesh.linkedin.notification_service.service.NotificationService;
import com.jagadeesh.linkedin.posts_service.event.PostCreatedEvent;
import com.jagadeesh.linkedin.posts_service.event.PostLikedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final NotificationService notificationService;


    @KafkaListener(topics="post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {

        log.info("Received PostCreatedEvent: {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());

        for(PersonDto connection: connections){
            notificationService.sendNotification(connection.getUserId(), "Your connections "+ postCreatedEvent.getCreatorId()+" a post, check it out!");
        }

    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {

        log.info("Received PostLikedEvent: {}", postLikedEvent);

        String message=String.format("Your Post %d had been liked by %d", postLikedEvent.getPostId(),postLikedEvent.getLikedByUserId());

        notificationService.sendNotification(postLikedEvent.getCreatorId(), message);
    }


}

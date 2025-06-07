package com.jagadeesh.linkedin.connections_service.service.impl;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.jagadeesh.linkedin.connections_service.auth.UserContextHolder;
import com.jagadeesh.linkedin.connections_service.entity.Person;
import com.jagadeesh.linkedin.connections_service.event.ConnectionRequestAcceptedEvent;
import com.jagadeesh.linkedin.connections_service.event.ConnectionRequestSentEvent;
import com.jagadeesh.linkedin.connections_service.repository.PersonRepository;
import com.jagadeesh.linkedin.connections_service.service.ConnectionsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService{

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long,ConnectionRequestSentEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long,ConnectionRequestAcceptedEvent> acceptedRequestKafkaTemplate;

    @Override
    public List<Person> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Fetching first degree connections for userId: {}", userId);
        
        return personRepository.getFirstDegreeConnectionsByUserId(userId);
    }

    @Override
    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId=UserContextHolder.getCurrentUserId();

        if(senderId.equals(receiverId)){
            log.info("Sender and receiver are the same user: userId: {}", senderId);
            throw new RuntimeException("Cannot send connection request to self");
        }

        if(personRepository.isConnectionRequestSent(senderId, receiverId)){
            log.info("Connection request already sent from userId: {} to userId: {}", senderId, receiverId);
            throw new RuntimeException("Connection request already sent");
        }
        if(personRepository.isConnected(senderId, receiverId)){
            log.info("Users are already connected: senderId: {}, receiverId: {}", senderId, receiverId);
            throw new RuntimeException("Users are already connected");
        }

        personRepository.sendConnectionRequest(senderId, receiverId);
        log.info("Connection request sent from userId: {} to userId: {}", senderId, receiverId);
        
        ConnectionRequestSentEvent event = ConnectionRequestSentEvent.builder()
                                            .senderId(senderId)
                                            .receiverId(receiverId)
                                            .build();
        sendRequestKafkaTemplate.send("connection-request-sent-topic", event);

        log.info("Connection request sent event published to Kafka topic");
        return true;
    }

    @Override
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        if(!personRepository.isConnectionRequestSent(senderId, receiverId)){
            log.info("No connection request found from userId: {} to userId: {}", senderId, receiverId);
            throw new RuntimeException("No connection request found");
        }
        personRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Connection request accepted from userId: {} to userId: {}", senderId, receiverId);

        ConnectionRequestAcceptedEvent event = ConnectionRequestAcceptedEvent.builder()
                                            .senderId(senderId)
                                            .receiverId(receiverId)
                                            .build();
        acceptedRequestKafkaTemplate.send("connection-request-accepted-topic", event);
        log.info("Connection request accepted event published to Kafka topic");
        return true;
    }
    @Override
    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        if(!personRepository.isConnectionRequestSent(senderId, receiverId)){
            log.info("No connection request found from userId: {} to userId: {}", senderId, receiverId);
            throw new RuntimeException("No connection request found");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);
        log.info("Connection request rejected from userId: {} to userId: {}", senderId, receiverId);
        return true;
    }

    @Override
    public Boolean deleteConnection(Long userId) {
        Long currentUserId = UserContextHolder.getCurrentUserId();

        if(!personRepository.isConnected(currentUserId, userId)){
            log.info("No connection found between userId: {} and userId: {}", currentUserId, userId);
            throw new RuntimeException("No connection found");
        }

        personRepository.deleteConnection(currentUserId, userId);
        log.info("Connection deleted between userId: {} and userId: {}", currentUserId, userId);
        return true;
    }


}

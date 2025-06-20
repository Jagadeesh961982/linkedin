package com.jagadeesh.linkedin.connections_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jagadeesh.linkedin.connections_service.entity.Person;
import com.jagadeesh.linkedin.connections_service.service.ConnectionsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {
    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree-connections")
    public ResponseEntity<List<Person>> getFirstDegreeConnections() {
        List<Person> connections = connectionsService.getFirstDegreeConnections();
        return ResponseEntity.ok(connections);
    }

    @PostMapping("/connectrequest/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
    }

    @PostMapping("/acceptrequest/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
    }

    @PostMapping("/rejectrequest/{userId}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
    }
    
    @PostMapping("/deleteconnection/{userId}")
    public ResponseEntity<Boolean> deleteConnection(@PathVariable Long userId) {
        return ResponseEntity.ok(connectionsService.deleteConnection(userId));
    }


}

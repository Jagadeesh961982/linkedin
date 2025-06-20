package com.jagadeesh.linkedin.connections_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestAcceptedEvent {
    private Long senderId; // ID of the user who sent the connection request
    private Long receiverId; // ID of the user who accepted the connection request
    
}

package com.jagadeesh.linkedin.connections_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestSentEvent {
    private Long senderId;
    private Long receiverId;
    
}

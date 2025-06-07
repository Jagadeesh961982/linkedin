package com.jagadeesh.linkedin.notification_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.jagadeesh.linkedin.notification_service.dto.PersonDto;

@FeignClient(name="connections-service",path="/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree-connections")
    List<PersonDto> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId);
}
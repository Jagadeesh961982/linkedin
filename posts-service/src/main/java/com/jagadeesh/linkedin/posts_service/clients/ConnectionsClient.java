package com.jagadeesh.linkedin.posts_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.jagadeesh.linkedin.posts_service.dto.PersonDto;

@FeignClient(name="connections-service",path="/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree")
    List<PersonDto> getFirstDegreeConnections();
}

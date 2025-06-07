package com.jagadeesh.linkedin.connections_service.service;

import java.util.List;

import com.jagadeesh.linkedin.connections_service.entity.Person;

public interface ConnectionsService {

    List<Person> getFirstDegreeConnections();

    Boolean sendConnectionRequest(Long userId);

    Boolean acceptConnectionRequest(Long userId);
    Boolean rejectConnectionRequest(Long userId);
    Boolean deleteConnection(Long userId);
}

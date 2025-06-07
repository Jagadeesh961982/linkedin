package com.jagadeesh.linkedin.connections_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.jagadeesh.linkedin.connections_service.entity.Person;

@Repository
public interface PersonRepository  extends Neo4jRepository<Person,Long>{

        Optional<Person> getByName(String name);

        @Query("MATCH (p:Person)-[r:CONNECTS_TO]-(c:Person) WHERE p.userId = $userId RETURN c")
        List<Person> getFirstDegreeConnectionsByUserId(Long userId);

        @Query("MATCH (p:Person)-[r:REQUESTED_TO]->(c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId RETURN count(r)>0")
        boolean isConnectionRequestSent(Long senderId, Long receiverId);

        @Query("MATCH (p:Person) - [r:CONNECTS_TO] - (c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId RETURN count(r) > 0")
        boolean isConnected(Long senderId, Long receiverId);

        @Query("Match (p:Person) ,(c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId CREATE (p)-[:REQUESTED_TO]->(c)")
        void sendConnectionRequest(Long senderId, Long receiverId);

         @Query("MATCH (p:Person) - [r:REQUESTED_TO] - (c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId DELETE r CREATE (p)-[:CONNECTS_TO]-(c)")
        void acceptConnectionRequest(Long senderId, Long receiverId);

        @Query("MATCH (p:Person) - [r:REQUESTED_TO] - (c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId DELETE r")
        void rejectConnectionRequest(Long senderId, Long receiverId);

        @Query("MATCH (p:Person) - [r:CONNECTS_TO] - (c:Person) WHERE p.userId=$senderId AND c.userId=$receiverId DELETE r")
        void deleteConnection(Long senderId, Long receiverId);
} 

package com.jagadeesh.linkedin.connections_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class Person {
    

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String name;

}

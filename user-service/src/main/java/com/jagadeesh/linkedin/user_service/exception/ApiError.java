package com.jagadeesh.linkedin.user_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {
    private String error;
    private HttpStatus status;
    private LocalDateTime timestamp;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus status){
        this();
        this.error=error;
        this.status=status;
    }
}

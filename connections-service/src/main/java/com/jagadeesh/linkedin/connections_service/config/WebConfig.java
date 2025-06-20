package com.jagadeesh.linkedin.connections_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jagadeesh.linkedin.connections_service.auth.UserInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(userInterceptor);
    }
}

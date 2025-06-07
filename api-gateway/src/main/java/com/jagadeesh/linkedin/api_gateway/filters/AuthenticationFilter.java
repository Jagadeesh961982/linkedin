package com.jagadeesh.linkedin.api_gateway.filters;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.jagadeesh.linkedin.api_gateway.service.JwtService;

import io.jsonwebtoken.JwtException;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;





@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

    private final JwtService jwtService;


    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("Applying Authentication Filter");
        return (exchange, chain) -> {
           final String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
           if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
           }
           String token= authorizationHeader.split("Bearer ")[1];
           try{
            String userId =jwtService.getUserIdFromToken(token);
            if(userId == null) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
            }
            ServerWebExchange mutatedExchange=exchange
                                                    .mutate()
                                                    .request(r->r.header("X-User-Id", userId))
                                                    .build();
           
            return chain.filter(mutatedExchange);
            }catch(JwtException e){
                log.error("Invalid token: {}", e.getLocalizedMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();

        }
        };
    }


    public static class Config{
        // this class can be used to define configuration properties for the filter
        // in application.yml or application.properties 
    }
}

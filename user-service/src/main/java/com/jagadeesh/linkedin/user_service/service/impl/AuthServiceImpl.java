package com.jagadeesh.linkedin.user_service.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jagadeesh.linkedin.user_service.dto.LoginRequestDto;
import com.jagadeesh.linkedin.user_service.dto.SignupDto;
import com.jagadeesh.linkedin.user_service.dto.UserDto;
import com.jagadeesh.linkedin.user_service.entity.User;
import com.jagadeesh.linkedin.user_service.exception.ResourceNotFoundException;
import com.jagadeesh.linkedin.user_service.repository.UserRepository;
import com.jagadeesh.linkedin.user_service.security.JwtService;
import com.jagadeesh.linkedin.user_service.service.AuthService;
import com.jagadeesh.linkedin.user_service.utils.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public UserDto signup(SignupDto signupDto){
        boolean userExists = userRepository.existsByEmail(signupDto.getEmail());
        if(userExists){
            throw new RuntimeException("User already exists");
        }
        User user=modelMapper.map(signupDto,User.class);
        user.setPassword(PasswordUtil.hashPassword(signupDto.getPassword()));
        User saveduser=userRepository.save(user);
        return modelMapper.map(saveduser,UserDto.class);
        
    }

    @Override
    public String login(LoginRequestDto loginRequestDto){
        User user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()->new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));
    
        if(!PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword())){
            throw new com.jagadeesh.linkedin.user_service.exception.BadRequestException("Invalid password");
        }

        return jwtService.getAccessToken(user);
    }  



}

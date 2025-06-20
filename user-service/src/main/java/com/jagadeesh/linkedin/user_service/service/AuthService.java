package com.jagadeesh.linkedin.user_service.service;

import com.jagadeesh.linkedin.user_service.dto.LoginRequestDto;
import com.jagadeesh.linkedin.user_service.dto.SignupDto;
import com.jagadeesh.linkedin.user_service.dto.UserDto;

public interface AuthService {

    public UserDto signup(SignupDto signupDto);

    public String login(LoginRequestDto loginRequestDto);
}

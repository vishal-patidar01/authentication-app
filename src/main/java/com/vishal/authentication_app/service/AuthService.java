package com.vishal.authentication_app.service;

import com.vishal.authentication_app.dto.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}

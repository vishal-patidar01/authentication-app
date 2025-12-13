package com.vishal.authentication_app.service;

import com.vishal.authentication_app.dto.UserDto;

import java.util.Iterator;
import java.util.UUID;

public interface UserService {

//    Create user
    UserDto createUser(UserDto userDto);

//    get user by email
    UserDto getUserByEmail(String email);

//    update user
    UserDto updateUser(UserDto userDto, String userId);

//    delete User
    void deleteUser(String userId);

//    get User by id
    UserDto getUserById(String userId);

//    get All User
    Iterable<UserDto> getAllUsers();
}

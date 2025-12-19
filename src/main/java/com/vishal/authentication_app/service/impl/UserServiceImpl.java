package com.vishal.authentication_app.service.impl;

import com.vishal.authentication_app.dto.UserDto;
import com.vishal.authentication_app.entities.Provider;
import com.vishal.authentication_app.entities.User;
import com.vishal.authentication_app.exceptions.ResourceNotFoundException;
import com.vishal.authentication_app.helpers.UserHelper;
import com.vishal.authentication_app.repositories.UserRepository;
import com.vishal.authentication_app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // if email is already existing
        if(userDto.getEmail()==null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given email already exist");
        }
        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
        // TODO
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given email id"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository.findById(uuid).orElseThrow(() ->
                new ResourceNotFoundException("User not found with given id"));

        if(userDto.getName() != null) existingUser.setName(userDto.getName());
        if(userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if(userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        // TODO
        if(userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());

        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
//        existingUser.setUpdateAt(Instant.now());
        User updateuser = userRepository.save(existingUser);
        return modelMapper.map(updateuser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uuid).orElseThrow(() ->
                new ResourceNotFoundException("User not found with given id"));

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uuid = UUID.fromString(userId);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given email id"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map((element) -> modelMapper.map(element, UserDto.class))
                .toList();
    }
}

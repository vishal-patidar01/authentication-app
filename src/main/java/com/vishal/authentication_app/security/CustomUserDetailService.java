package com.vishal.authentication_app.security;

import com.vishal.authentication_app.entities.User;
import com.vishal.authentication_app.exceptions.ResourceNotFoundException;
import com.vishal.authentication_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        return userRepository.findByEmail(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Invalid Email or password"));
//    }
//}


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid email or password"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // encrypted password
                .authorities("ROLE_USER")
                .build();
    }
}
package com.springboot.blog.service;

import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(User user) {
        try {
            user.setRole(UserRole.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("server error");
        }

        HttpStatus created = HttpStatus.CREATED;
        ApiResponse success = new ApiResponse(created, "Welcome to Blog", System.currentTimeMillis());


        return new ResponseEntity<>(success, created);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException
                    ("Email " + email + " is already taken");
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "Email " + email + " is available", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(User user) {
        User found_user = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new IllegalArgumentException("not found user"));
        found_user.setUsername(user.getUsername());
        found_user.setPassword(passwordEncoder.encode(user.getPassword()));

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email " + email + " is not found"));
        return user;
    }

}
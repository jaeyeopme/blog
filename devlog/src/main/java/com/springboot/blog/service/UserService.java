package com.springboot.blog.service;

import com.springboot.blog.controller.rest.UserRestController;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<String> signup(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다.");

        newUser.setRole(UserRole.ROLE_USER);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Long userId = userRepository.save(newUser).getId();

        return ResponseEntity.created(linkTo(methodOn(UserRestController.class)
                .signup(newUser)).slash(userId).withSelfRel().toUri()).body("{}");
    }

    @Transactional
    public ResponseEntity<?> update(User newUser, Long id) {
        return userRepository.findById(id)
                .map(user -> { // stream
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    user.setIntroduction(newUser.getIntroduction());
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> deleteById(Long id) {
//        try {
//            userRepository.deleteById(id);
//        } catch (Exception e) {
//            new IllegalArgumentException(String.format("not found user - %d", id));
//        }
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일을 확인해주세요."));
    }

}
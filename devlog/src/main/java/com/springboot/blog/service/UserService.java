package com.springboot.blog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.springboot.blog.common.AmazonService;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AmazonService amazonService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmazonS3 amazonS3, AmazonService amazonService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.amazonService = amazonService;
    }

    @Transactional
    public User signup(User newUser) {
        userRepository.findByEmail(newUser.getEmail())
                .ifPresent(findUser -> new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."));

        newUser.setRole(UserRole.ROLE_USER);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    @Transactional
    public User modify(Long id, User newUser, MultipartFile newPhoto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setIntroduction(newUser.getIntroduction());

                    if (!newPhoto.isEmpty()) {
                        if (!user.getPhoto().isEmpty()) {
                            amazonService.deletePhoto(user.getPhoto());
                        }
                        user.setPhoto(amazonService.putPhoto(newPhoto));
                    }

                    return user;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    if (!user.getPhoto().isEmpty()) {
                        amazonService.deletePhoto(user.getPhoto());
                    }

                    user.getBoards()
                            .stream()
                            .filter(board -> !board.getPhoto().isEmpty())
                            .forEach(board -> amazonService.deletePhoto(board.getPhoto()));

                    userRepository.deleteById(user.getId());
                    return user;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일을 찾을 수 없습니다."));
    }

}
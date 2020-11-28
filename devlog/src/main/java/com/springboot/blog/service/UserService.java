package com.springboot.blog.service;

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
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmazonService amazonService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.amazonService = amazonService;
    }

    @Transactional
    public User join(User newUser) {
        return (User) userRepository.findByEmail(newUser.getEmail())
                .map(findUser -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Email <strong>%s</strong> is not available.", findUser.getEmail()));
                })
                .orElseGet(() -> {
                    newUser.setRole(UserRole.ROLE_USER);
                    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(newUser);
                });
    }

    @Transactional
    public User modify(Long id, User newUser, MultipartFile newPhoto) {
        return userRepository.findById(id)
                .map(findUser -> {
                    findUser.setIntroduce(newUser.getIntroduce());

                    if (!newPhoto.isEmpty()) {
                        if (!findUser.getPhoto().isEmpty()) {
                            amazonService.deletePhoto(findUser.getPhoto());
                        }
                        amazonService.putPhoto(newPhoto);
                    }

                    return findUser;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user."));
    }

    @Transactional
    public User delete(Long id) {
        return userRepository.findById(id)
                .map(findUser -> {
                    if (!findUser.getPhoto().isEmpty()) {
                        amazonService.deletePhoto(findUser.getPhoto());
                    }

                    findUser.getBoards()
                            .stream()
                            .filter(board -> !board.getPhoto().isEmpty())
                            .forEach(board -> amazonService.deletePhoto(board.getPhoto()));

                    userRepository.deleteById(findUser.getId());

                    return findUser;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user."));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user."));
    }

}
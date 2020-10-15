package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.vo.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Response> join(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Email is invalid or already taken");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Response> login(User user, HttpSession session) {
        User principal = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).
                orElseThrow(() -> new RuntimeException("Incorrect username or password."));

        session.setAttribute("principal", principal);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

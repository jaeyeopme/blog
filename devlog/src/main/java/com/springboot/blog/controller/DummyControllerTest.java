package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class DummyControllerTest {

    private final UserRepository userRepository;

    public DummyControllerTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("users")
    public List<User> findAll(@PageableDefault(size = 2, sort = "id") Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @GetMapping("users/{userId}")
    public User findById(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));

        return user;
    }

    @PostMapping("users")
    public String save(@RequestBody User user) {
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);

        return "회원가입 완료";
    }

    @Transactional
    @PutMapping("users/{userId}")
    public User update(@RequestBody User requestUser, @PathVariable(name = "userId") Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        return user;
    }

    @DeleteMapping("users/{userId}")
    public String delete(@PathVariable(name = "userId") Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));
        userRepository.delete(user);

        return "회원삭제 완료";
    }

}

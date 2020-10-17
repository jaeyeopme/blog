package com.springboot.blog.controller;

import com.springboot.blog.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("join")
    public String joinForm() {
        return "user/join-form";
    }

    @GetMapping("login")
    public String loginForm() {
        return "user/login-form";
    }

    @GetMapping("logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:";
    }



//    @GetMapping("/")
//    public List<User> findAll(@PageableDefault(size = 2, sort = "id") Pageable pageable) {
//        return  userRepository.findAll(pageable).getContent();
//    }
//
//    @GetMapping("/{userId}")
//    public User findById(@PathVariable("userId") Long userId) {
//        User user = userRepository.findById(userId).
//                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));
//
//        return user;
//    }
//
//    @PostMapping("/")
//    public String save(@RequestBody User user) {
//        user.setRole(UserRole.ROLE_USER);
//        userRepository.save(user);
//
//        return "회원가입 완료";
//    }
//
//    @Transactional
//    @PutMapping("/{userId}")
//    public User update(@RequestBody User requestUser, @PathVariable(name = "userId") Long userId) {
//        User user = userRepository.findById(userId).
//                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));
//        user.setPassword(requestUser.getPassword());
//        user.setEmail(requestUser.getEmail());
//
//        return user;
//    }
//
//    @DeleteMapping("users/{userId}")
//    public String delete(@PathVariable(name = "userId") Long userId) {
//        User user = userRepository.findById(userId).
//                orElseThrow(() -> new IllegalArgumentException("not found - " + userId));
//        userRepository.delete(user);
//
//        return "회원삭제 완료";
//    }

}

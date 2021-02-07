package com.springboot.blog.service;

import com.springboot.blog.entity.User;

public interface UserService {

    void registration(String email);

    User registrationConfirmToken(String token);

}

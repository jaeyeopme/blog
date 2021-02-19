package com.springboot.blog.security;

public interface AuthenticationService {

    String sendSignupToken(String validEmail);

    String sendLoginToken(String validEmail);

    void signup(String token);

    String login(String token);

}

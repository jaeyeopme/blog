package com.springboot.blog.security;

import com.springboot.blog.domain.User;

public interface AuthenticationService {

    void join(String email);

    void login(String email);

    User joinConfirmation(String email);

    User loginConfirmation(String email);

}

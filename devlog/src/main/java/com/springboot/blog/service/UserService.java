package com.springboot.blog.service;

import com.springboot.blog.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User save(User user);

    void modify(Long id, User user, MultipartFile picture);

    void delete(Long id);

    User findById(Long id);

    User findByEmail(String email);

    Boolean existsByEmail(String email);

}

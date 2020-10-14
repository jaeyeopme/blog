package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

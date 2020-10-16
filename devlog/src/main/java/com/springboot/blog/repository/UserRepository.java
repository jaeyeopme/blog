package com.springboot.blog.repository;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "select * from user where email=? and password=?", nativeQuery = true)
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

}

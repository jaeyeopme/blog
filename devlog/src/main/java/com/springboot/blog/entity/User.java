package com.springboot.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

// @DynamicInsert -> except null
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true, nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 45)
    private String username;

    @Column(nullable = false, length = 68)
    private String password;

    @CreationTimestamp
    private Timestamp createdAt;

}

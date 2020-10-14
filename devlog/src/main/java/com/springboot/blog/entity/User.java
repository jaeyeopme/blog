package com.springboot.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ColumnDefault("'USER_ROLE'")
    private String role;

    @Column(nullable = false, length = 45)
    private String username;

    @Column(nullable = false, length = 68)
    private String password;

    @Column(nullable = false, length = 45)
    private String email;

    @CreationTimestamp
    private Timestamp createdAt;

}

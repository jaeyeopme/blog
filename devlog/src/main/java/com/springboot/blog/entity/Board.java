package com.springboot.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    private String thumbnailUrl;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne // default fetch = FetchType.EAGER
    @JoinColumn(name = "userId")
    private User user;

    @OrderBy(value = "id desc")
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) // default fetch = FetchType.LAZY
    private List<Reply> replies;

}

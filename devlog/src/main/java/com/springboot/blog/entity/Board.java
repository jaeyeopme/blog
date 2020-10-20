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
public class Board {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

//    @Lob
//    private String thumbnail;

    @ColumnDefault("0")
    private Long views;

    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne // default fetch = FetchType.EAGER
    @JoinColumn(name = "userId")
    private User user;

    @OrderBy(value = "id desc")
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY) // default fetch = FetchType.LAZY
    private List<Reply> replies;

}

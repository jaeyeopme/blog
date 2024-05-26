package com.springboot.blog.service;

import com.springboot.blog.domain.Board;
import com.springboot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    void write(User user, Board board, MultipartFile image);


    void edit(Long id, Board board, MultipartFile image);

    void delete(Long id);

    Board findById(Long id);

    Page<Board> findAll(Pageable pageable);

}

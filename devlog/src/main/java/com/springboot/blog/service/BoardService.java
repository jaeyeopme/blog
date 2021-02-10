package com.springboot.blog.service;

import com.springboot.blog.domain.Board;
import com.springboot.blog.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    void write(Long userId, Board newBoard, MultipartFile newImage);

    void edit(Long id, Board newBoard, MultipartFile newImage);

    void delete(Long id);

}

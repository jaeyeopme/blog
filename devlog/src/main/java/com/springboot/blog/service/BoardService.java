package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.Board;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    void write(User user, Board newBoard, MultipartFile newImage);

    void edit(Long id, Board newBoard, MultipartFile newImage);

    void delete(Long id);

}

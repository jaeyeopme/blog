package com.springboot.blog.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.springboot.blog.config.AmazonConfig;
import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonConfig amazonConfig;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public BoardService(BoardRepository boardRepository, AmazonConfig amazonConfig) {
        this.boardRepository = boardRepository;
        this.amazonConfig = amazonConfig;
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Board board, MultipartFile multipartFile, User user) {
        try {
            if (multipartFile != null) {
                UUID uuid = UUID.randomUUID();

                String fileName = uuid + "-" + multipartFile.getOriginalFilename();

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(multipartFile.getContentType());
                objectMetadata.setContentLength(multipartFile.getSize());

                amazonConfig.amazonS3().putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), objectMetadata));

                board.setThumbnail(fileName);
            }

            board.setUser(user);
            boardRepository.save(board);
        } catch (Exception e) {
            throw new IllegalArgumentException("server error");
        }

        HttpStatus created = HttpStatus.CREATED;
        ApiResponse success = new ApiResponse(created, "/detail/" + board.getId(), System.currentTimeMillis());

        return new ResponseEntity<>(success, created);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardsId) {
        return boardRepository.findById(boardsId).orElseThrow(() -> new IllegalArgumentException("not found board"));
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Board board, MultipartFile file) {
        Board found_board = boardRepository.findById(board.getId()).orElseThrow(() -> new IllegalArgumentException("not found board"));
        found_board.setTitle(board.getTitle());
        found_board.setContent(board.getContent());
        found_board.setDescription(board.getDescription());

        if (file != null) {
            try {
                if (found_board.getThumbnail() != null) {
                    new File("C:\\Users\\Jaeyeop\\IdeaProjects\\blog\\src\\main\\resources\\static\\images\\" + found_board.getThumbnail()).delete();
                }

                UUID uuid = UUID.randomUUID();

                String absolutePath = "C:\\Users\\Jaeyeop\\IdeaProjects\\blog\\src\\main\\resources\\static\\images\\" + uuid + "_" + file.getOriginalFilename();
                file.transferTo(new File(absolutePath));

                String relativePath = uuid + "_" + file.getOriginalFilename();
                found_board.setThumbnail(relativePath);
            } catch (Exception e) {
                throw new IllegalArgumentException("server error");
            }
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "/detail/" + board.getId(), System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        Board found_board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found board"));

        try {
            if (found_board.getThumbnail() != null) {
                new File("C:\\Users\\Jaeyeop\\IdeaProjects\\blog\\src\\main\\resources\\static\\images\\" + found_board.getThumbnail()).delete();
            }
            boardRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("not found board");
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

}

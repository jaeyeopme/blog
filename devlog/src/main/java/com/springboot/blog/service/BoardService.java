package com.springboot.blog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.springboot.blog.controller.rest.BoardRestController;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.objectUrl}")
    private String objectUrl;

    @Autowired
    public BoardService(BoardRepository boardRepository, AmazonS3 amazonS3) {
        this.boardRepository = boardRepository;
        this.amazonS3 = amazonS3;
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseEntity<String> write(Board board, MultipartFile thumbnail, User user) {
        if (thumbnail != null) {
            String thumbnailName = String.format("images/%s-%s", UUID.randomUUID(), thumbnail.getOriginalFilename());
            putThumbnail(thumbnail, thumbnailName);
            board.setThumbnailUrl(String.format("%s%s", objectUrl, thumbnailName));
        }

        board.setUser(user);
        Long id = boardRepository.save(board).getId();

        return ResponseEntity.created(linkTo(methodOn(BoardRestController.class)
                .write(board, thumbnail, user)).slash(id).withSelfRel().toUri()).body("{}");
    }

    public ResponseEntity<String> modify(Long id, Board newBoard, MultipartFile newThumbnail) {
        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    board.setIntroduction(newBoard.getIntroduction());

                    if (newThumbnail != null) {
                        if (board.getThumbnailUrl() != null) {
                            deleteThumbnail(board.getThumbnailUrl());
                        }
                        String newThumbnailName = String.format("images/%s-%s", UUID.randomUUID(), newThumbnail.getOriginalFilename());
                        putThumbnail(newThumbnail, newThumbnailName);
                        board.setThumbnailUrl(String.format("%s%s", objectUrl, newThumbnailName));
                    }

                    return ResponseEntity.ok().header("Location", "/boards/" + board.getId()).body("{}");
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."));
    }

    //    @Transactional
//    public ResponseEntity<String> update(Board board, MultipartFile file) {
//        Board found_board = boardRepository.findById(board.getId()).orElseThrow(() -> new IllegalArgumentException(String.format("not found board - %d", board.getId())));
//
//        if (file != null) {
//            String thumbnailUrl = found_board.getThumbnailUrl();
//            if (thumbnailUrl != null) {
//                deleteThumbnail(thumbnailUrl);
//            }
//            String filename = String.format("images/%s-%s", UUID.randomUUID(), file.getOriginalFilename());
//            putThumbnail(file, filename);
//            found_board.setThumbnailUrl(String.format("%s%s", objectUrl, filename));
//        }
//
//        found_board.setTitle(board.getTitle());
//        found_board.setContent(board.getContent());
//        found_board.setDescription(board.getDescription());
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "/detail/" + board.getId(), System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> deleteById(Long id) {
//        Board found_board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found board"));
//        String thumbnailUrl = found_board.getThumbnailUrl();
//
//        if (thumbnailUrl != null) {
//            deleteThumbnail(thumbnailUrl);
//        }
//
//        try {
//            boardRepository.deleteById(id);
//        } catch (Exception e) {
//            throw new IllegalArgumentException(String.format("not found board - %d", id));
//        }
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }
//
    private void putThumbnail(MultipartFile thumbnail, String thumbnailName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(thumbnail.getContentType());
        metadata.setContentLength(thumbnail.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, thumbnailName, thumbnail.getInputStream(), metadata));
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 저장에 실패했습니다.");
        }

    }

    private void deleteThumbnail(String thumbnail) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnail.replace(objectUrl, "")));
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다.");
        }
    }

}

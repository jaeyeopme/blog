package com.springboot.blog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.springboot.blog.entity.Board;
import com.springboot.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    @Transactional
//    public ResponseEntity<ApiResponse> save(Board board, MultipartFile file, User user) {
//
//        if (file != null) {
//            String filename = String.format("images/%s-%s", UUID.randomUUID(), file.getOriginalFilename());
//            putThumbnail(file, filename);
//            board.setThumbnailUrl(String.format("%s%s", objectUrl, filename));
//        }
//
//        try {
//            board.setUser(user);
//            boardRepository.save(board);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("server error");
//        }
//
//        HttpStatus created = HttpStatus.CREATED;
//        ApiResponse success = new ApiResponse(created, String.format("/detail/%d", board.getId()), System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, created);
//    }
//
//    @Transactional(readOnly = true)
//    public Board findById(Long id) {
//        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("not found board - %d", id)));
//    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> update(Board board, MultipartFile file) {
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
//    private void putThumbnail(MultipartFile file, String filename) {
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentType(file.getContentType());
//        metadata.setContentLength(file.getSize());
//
//        try {
//            amazonS3.putObject(new PutObjectRequest(bucket, filename, file.getInputStream(), metadata));
//        } catch (AmazonServiceException | IOException e) {
//            throw new IllegalArgumentException("Failed to store file to S3", e);
//        }
//
//    }
//
//    private void deleteThumbnail(String thumbnailUrl) {
//        try {
//            amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnailUrl.replace(objectUrl, "")));
//        } catch (AmazonServiceException e) {
//            throw new IllegalArgumentException("Failed to delete file to S3", e);
//        }
//    }

}

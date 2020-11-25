package com.springboot.blog.service;

import com.springboot.blog.common.AmazonService;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AmazonService amazonService;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, AmazonService amazonService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.amazonService = amazonService;
    }

    @Transactional
    public Board write(User newUser, Board newBoard, MultipartFile newPhoto) {
        newBoard.setUser(userRepository.findById(newUser.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")));

        if (!newPhoto.isEmpty()) {
            newBoard.setPhoto(amazonService.putPhoto(newPhoto));
        }

        return boardRepository.save(newBoard);
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

//    @Transactional
//    public ResponseEntity<String> modify(Long id, Board newBoard, MultipartFile newThumbnail) {
//        return boardRepository.findById(id).map(board -> {
//            board.setTitle(newBoard.getTitle());
//            board.setContent(newBoard.getContent());
//            board.setIntroduction(newBoard.getIntroduction());
//            deleteThumbnail(board.getThumbnailUrl());
//            putThumbnail(board, newThumbnail);
//
//            return ResponseEntity.ok().header("Location", "/boards/" + board.getId()).body("{}");
//        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
//    }
//
//    @Transactional
//    public ResponseEntity<String> deleteById(Long id) {
//        deleteThumbnail(boardRepository.findById(id).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.")
//        ).getThumbnailUrl());
//        boardRepository.deleteById(id);
//
//        return ResponseEntity.ok("{}");
//    }
//
//    private void putThumbnail(Board board, MultipartFile thumbnail) {
//        if (thumbnail != null) {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(thumbnail.getContentType());
//            metadata.setContentLength(thumbnail.getSize());
//
//            try {
//                String thumbnailName = String.format("images/%s-%s", UUID.randomUUID(), thumbnail.getOriginalFilename());
//                amazonS3.putObject(new PutObjectRequest(bucket, thumbnailName, thumbnail.getInputStream(), metadata));
//                board.setThumbnailUrl(String.format("%s%s", url, thumbnailName));
//            } catch (Exception e) {
//                throw new IllegalArgumentException("사진 저장에 실패했습니다.");
//            }
//        }
//    }
//
//    private void deleteThumbnail(String thumbnail) {
//        try {
//            if (thumbnail != null && !thumbnail.equals(defaultImage))
//                amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnail.replace(url, "")));
//        } catch (Exception e) {
//            throw new IllegalArgumentException("사진 삭제에 실패했습니다.");
//        }
//    }

}

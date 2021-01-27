package com.springboot.blog.service;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.util.AmazonService;
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

    private final String NOT_FOUND_MESSAGE = "Not found %s.";
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AmazonService amazonService;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, AmazonService amazonService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.amazonService = amazonService;
    }

    /**
     * 게시글 작성
     *
     * @param user
     * @param newBoard
     * @param newImage
     * @return
     */
    @Transactional
    public Board write(User user, Board newBoard, MultipartFile newImage) {
        return userRepository.findById(user.getId())
                .map(findUser -> {
                    newBoard.setUser(findUser);

                    if (newImage != null) {
                        newBoard.setImage(amazonService.putImage(newImage));
                    }

                    return boardRepository.save(newBoard);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "user")));
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "board")));
    }

    /**
     * 게시글 수정
     *
     * @param id
     * @param newBoard
     * @param newImage
     * @return
     */
    @Transactional
    public Board edit(Long id, Board newBoard, MultipartFile newImage) {
        return boardRepository.findById(id)
                .map(findBoard -> {
                    findBoard.setTitle(newBoard.getTitle());
                    findBoard.setContent(newBoard.getContent());
                    findBoard.setIntroduce(newBoard.getIntroduce());

                    if (newImage != null) {
                        if (!findBoard.getImage().isEmpty()) {
                            amazonService.deleteImage(findBoard.getImage());
                        }
                        findBoard.setImage(amazonService.putImage(newImage));
                    }

                    return findBoard;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "board")));
    }

    /**
     * 게시글 삭제
     *
     * @param id
     * @return
     */
    @Transactional
    public void delete(Long id) {
        boardRepository.findById(id)
                .map(findBoard -> {
                    if (findBoard.getImage() != null) {
                        amazonService.deleteImage(findBoard.getImage());
                    }

                    boardRepository.deleteById(findBoard.getId());

                    return findBoard;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "board")));
    }

}

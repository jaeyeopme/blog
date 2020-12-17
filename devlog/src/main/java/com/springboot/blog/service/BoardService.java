package com.springboot.blog.service;

import com.springboot.blog.util.AmazonService;
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

    /**
     * 게시글 작성
     *
     * @param user
     * @param newBoard
     * @param newPhoto
     * @return
     */
    @Transactional
    public Board write(User user, Board newBoard, MultipartFile newPhoto) {
        return userRepository.findById(user.getId())
                .map(findUser -> {
                    newBoard.setUser(findUser);

                    if (newPhoto != null) {
                        newBoard.setPhoto(amazonService.putPhoto(newPhoto));
                    }

                    return boardRepository.save(newBoard);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user."));
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

    /**
     * 게시글 수정
     *
     * @param id
     * @param newBoard
     * @param newPhoto
     * @return
     */
    @Transactional
    public Board edit(Long id, Board newBoard, MultipartFile newPhoto) {
        return boardRepository.findById(id)
                .map(findBoard -> {
                    findBoard.setTitle(newBoard.getTitle());
                    findBoard.setContent(newBoard.getContent());
                    findBoard.setIntroduce(newBoard.getIntroduce());

                    if (newPhoto != null) {
                        if (!findBoard.getPhoto().isEmpty()) {
                            amazonService.deletePhoto(findBoard.getPhoto());
                        }
                        findBoard.setPhoto(amazonService.putPhoto(newPhoto));
                    }

                    return findBoard;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
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
                    if (findBoard.getPhoto() != null) {
                        amazonService.deletePhoto(findBoard.getPhoto());
                    }

                    boardRepository.deleteById(findBoard.getId());

                    return findBoard;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

}

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
        newBoard.setUser(userRepository.findById(newUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user.")));

        if (newPhoto != null) {
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
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

    @Transactional
    public Board edit(Long id, Board newBoard, MultipartFile newPhoto) {
        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    board.setIntroduce(newBoard.getIntroduce());

                    if (newPhoto != null) {
                        if (!board.getPhoto().isEmpty()) {
                            amazonService.deletePhoto(board.getPhoto());
                        }
                        board.setPhoto(amazonService.putPhoto(newPhoto));
                    }

                    return board;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

    @Transactional
    public Board delete(Long id) {
        return boardRepository.findById(id)
                .map(board -> {
                    if (!board.getPhoto().isEmpty()) {
                        amazonService.deletePhoto(board.getPhoto());
                    }

                    boardRepository.deleteById(board.getId());

                    return board;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

}

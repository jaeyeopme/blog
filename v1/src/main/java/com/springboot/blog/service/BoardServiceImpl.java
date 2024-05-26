package com.springboot.blog.service;

import com.springboot.blog.domain.Board;
import com.springboot.blog.domain.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.util.AmazonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.springboot.blog.config.SecurityConfig.NOT_FOUND_MESSAGE_FORMAT;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AmazonService amazonService;

    public BoardServiceImpl(BoardRepository boardRepository,
                            AmazonService amazonService) {
        this.boardRepository = boardRepository;
        this.amazonService = amazonService;
    }

    @Transactional
    @Override
    public void write(User user, Board board, MultipartFile image) {
        board.setUser(user);

        Optional.ofNullable(image)
                .ifPresent(newImage ->
                        board.setImageUrl(amazonService.saveImage(newImage)));

        boardRepository.save(board);
    }

    @Transactional
    @Override
    public void edit(Long id, Board board, MultipartFile image) {
        boardRepository.findById(id)
                .map(findBoard -> {
                    findBoard.setTitle(board.getTitle());
                    findBoard.setContent(board.getContent());
                    findBoard.setIntroduce(board.getIntroduce());

                    Optional.ofNullable(image)
                            .ifPresent(newImage ->
                                    findBoard.setImageUrl(amazonService.changeImage(newImage, findBoard.getImageUrl())));

                    return findBoard;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE_FORMAT, "board")));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        boardRepository.findById(id)
                .map(findBoard -> {
                    Optional.ofNullable(findBoard.getImageUrl())
                            .ifPresent(amazonService::deleteImage);

                    boardRepository.deleteById(findBoard.getId());

                    return findBoard;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE_FORMAT, "board")));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE_FORMAT, "board")));
    }

}

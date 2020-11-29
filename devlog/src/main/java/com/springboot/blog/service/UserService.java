package com.springboot.blog.service;

import com.springboot.blog.common.AmazonService;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AmazonService amazonService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmazonService amazonService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.amazonService = amazonService;
    }

    /**
     * 회원 가입
     *
     * @param newUser
     * @return
     */
    @Transactional
    public User join(User newUser) {
        User validUser = userValidation(newUser);

        validUser.setRole(UserRole.ROLE_USER);
        validUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(validUser);
    }

    /**
     * 회원 수정
     *
     * @param id
     * @param newUser
     * @param newPhoto
     * @return
     */
    @Transactional
    public User modify(Long id, User newUser, MultipartFile newPhoto) {
        return userRepository.findById(id)
                .map(findUser -> {
                    findUser.setName(newUser.getName());
                    findUser.setIntroduce(newUser.getIntroduce());

                    if (newPhoto != null) {
                        if (findUser.getPhoto() != null) {
                            amazonService.deletePhoto(findUser.getPhoto());
                        }
                        findUser.setPhoto(amazonService.putPhoto(newPhoto));
                    }

                    return findUser;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user."));
    }

    /**
     * 회원 삭제
     *
     * @param id
     * @return
     */
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id)
                .map(findUser -> {
                    if (findUser.getPhoto() != null) {
                        amazonService.deletePhoto(findUser.getPhoto());
                    }

                    findUser.getBoards()
                            .stream()
                            .filter(board -> board.getPhoto() != null)
                            .forEach(board -> amazonService.deletePhoto(board.getPhoto()));

                    userRepository.deleteById(findUser.getId());

                    return findUser;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user."));
    }

    /**
     * 회원 유효성 검사
     *
     * @param newUser
     * @return
     */
    @Transactional
    public User userValidation(User newUser) {
        userRepository.findByUsername(newUser.getUsername()).ifPresent(
                findUser -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Username <strong>%s</strong> is already taken.", findUser.getUsername()));
                });
//        userRepository.findByEmail(newUser.getEmail()).ifPresent(
//                findUser -> {
//                    throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Email <strong>%s</strong> is already taken.", findUser.getEmail()));
//                });

        return newUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user."));
    }

}
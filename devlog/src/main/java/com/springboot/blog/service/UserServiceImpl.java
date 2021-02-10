package com.springboot.blog.service;

import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.AuthenticationServiceImpl;
import com.springboot.blog.security.JwtService;
import com.springboot.blog.util.AmazonService;
import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String FOUND_EMAIL_MESSAGE = "Email <strong>%s</strong> is already taken.";
    private static final String NOT_FOUND_EMAIL_MESSAGE = "Email is not found.";

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final AmazonService amazonService;
    private final String defaultPicture;

    public UserServiceImpl(UserRepository userRepository, AmazonService amazonService, JwtService jwtService, MailService mailService, RedisService redisService, AuthenticationServiceImpl authenticationServiceImpl, @Value("${cloud.aws.s3.default-picture}") String defaultPicture) {
        this.userRepository = userRepository;
        this.amazonService = amazonService;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.defaultPicture = defaultPicture;
    }

//    @Transactional(readOnly = true)
//    @Override
//    public void signUp(String email) {
//        authenticationService.sendToken("Sign up", email);
//    }
//
//    @Transactional
//    @Override
//    public User signUpConfirm(String token) {
//        String authenticatedEmail = authenticationService.getEmailFromToken(token);
//
//        return userRepository.save(
//                User.builder()
//                        .role(Role.ROLE_USER)
//                        .email(authenticatedEmail)
//                        .picture(defaultPicture)
//                        .build());
//    }

//    @Override
//    public void login(String email) {
//    }
//
//    @Override
//    public User loginConfirmToken(String token) {
//        String verifiedEmail = authenticationService.getEmailFromToken(token); // 검증
//
//        return null;
//    }


    @Transactional(readOnly = true)
    private String notExistsEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(FOUND_EMAIL_MESSAGE, email));

        return email;
    }
//
//    @Transactional(readOnly = true)
//    private String existsEmail(String email) {
//        if (!userRepository.existsByEmail(email))
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_EMAIL_MESSAGE);
//
//        return email;
//    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_EMAIL_MESSAGE));
    }

//    private String getPicture(OAuth2User oAuth2User, String authId, String registrationId) {
//        String picture = null;
//
//        switch (registrationId) {
//            case "google":
//                picture = oAuth2User.getAttribute("picture");
//                break;
//            case "github":
//                picture = oAuth2User.getAttribute("avatar_url");
//                break;
//            case "facebook":
//                picture = String.format("https://graph.facebook.com/%s/picture?type=normal", authId);
//                break;
//        }
//
//        return picture;
//    }

//    /**
//     * 회원 수정
//     *
//     * @param id
//     * @param newUser
//     * @param newPicture
//     * @return
//     */
//    @Transactional
//    @Override
//    public User modify(Long id, User newUser, MultipartFile newPicture) {
//        return userRepository.findById(id)
//                .map(findUser -> {
//                    findUser.setName(newUser.getName());
//                    findUser.setIntroduce(newUser.getIntroduce());
//
//                    if (newPicture != null) {
//                        if (findUser.getPicture() != null) {
//                            amazonService.deleteImage(findUser.getPicture());
//                        }
//                        findUser.setPicture(amazonService.putImage(newPicture));
//                    }
//
//                    return findUser;
//                })
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));
//    }
//
//    /**
//     * 회원 삭제
//     *
//     * @param id
//     * @return
//     */
//    @Transactional
//    @Override
//    public void delete(Long id) {
//        userRepository.findById(id)
//                .map(findUser -> {
//                    if (findUser.getPicture() != null) {
//                        amazonService.deleteImage(findUser.getPicture());
//                    }
//
//                    findUser.getBoards()
//                            .stream()
//                            .filter(board -> board.getImage() != null)
//                            .forEach(board -> amazonService.deleteImage(board.getImage()));
//
//                    userRepository.deleteById(findUser.getId());
//
//                    return findUser;
//                })
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));
//    }

}

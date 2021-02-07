package com.springboot.blog.service;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtService;
import com.springboot.blog.service.verifycationService.VerificationService;
import com.springboot.blog.util.AmazonService;
import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private static final String FOUND_EMAIL_MESSAGE = "Email <strong>%s</strong> is already taken.";
    private static final String NOT_FOUND_EMAIL_MESSAGE = "Email is not found.";

    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final AmazonService amazonService;
    private final String defaultPicture;

    public UserServiceImpl(UserRepository userRepository, AmazonService amazonService, JwtService jwtService, MailService mailService, RedisService redisService, VerificationService verificationService, @Value("${cloud.aws.s3.default-picture}") String defaultPicture) {
        this.userRepository = userRepository;
        this.amazonService = amazonService;
        this.verificationService = verificationService;
        this.defaultPicture = defaultPicture;
    }

    @Override
    public void registration(String email) {
        String validEmail = notExistsEmail(email);

        verificationService.register(validEmail);
    }

    @Transactional
    @Override
    public User registrationConfirmToken(String token) {
        String verifiedEmail = verificationService.confirmToken(token);

        return userRepository.save(
                User.builder()
                        .role(Role.ROLE_USER)
                        .email(verifiedEmail)
                        .picture(defaultPicture)
                        .build());

    }

    @Transactional(readOnly = true)
    public User signInConfirm(String token) {
        String email = verificationService.confirmToken(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_EMAIL_MESSAGE));
    }

    @Transactional(readOnly = true)
    private String notExistsEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(FOUND_EMAIL_MESSAGE, email));

        return email;
    }

    @Transactional(readOnly = true)
    private String existsEmail(String email) {
        if (!userRepository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_EMAIL_MESSAGE);

        return email;
    }

    private String getPicture(OAuth2User oAuth2User, String authId, String registrationId) {
        String picture = null;

        switch (registrationId) {
            case "google":
                picture = oAuth2User.getAttribute("picture");
                break;
            case "github":
                picture = oAuth2User.getAttribute("avatar_url");
                break;
            case "facebook":
                picture = String.format("https://graph.facebook.com/%s/picture?type=normal", authId);
                break;
        }

        return picture;
    }

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

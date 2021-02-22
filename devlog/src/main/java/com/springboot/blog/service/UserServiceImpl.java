package com.springboot.blog.service;

import com.springboot.blog.domain.User;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.util.AmazonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.springboot.blog.config.SecurityConfig.NOT_FOUND_MESSAGE_FORMAT;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AmazonService amazonService;

    private final String defaultPicture;

    public UserServiceImpl(UserRepository userRepository,
                           AmazonService amazonService,
                           @Value("${cloud.aws.s3.default-picture}") String defaultPicture) {
        this.userRepository = userRepository;
        this.amazonService = amazonService;
        this.defaultPicture = defaultPicture;
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void modify(Long id, User user, MultipartFile picture) {
        userRepository.findById(id)
                .map(findUser -> {
                            findUser.setName(user.getName());
                            findUser.setIntroduce(user.getIntroduce());

                            Optional.ofNullable(picture)
                                    .ifPresent(newPicture ->
                                            findUser.setPictureUrl(amazonService.changeImage(newPicture, findUser.getPictureUrl())));

                            return user;
                        }
                )
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(NOT_FOUND_MESSAGE_FORMAT, "user")));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.findById(id)
                .map(findUser -> {
                    Optional.ofNullable(findUser.getPictureUrl())
                            .ifPresent(amazonService::deleteImage);

                    findUser.getBoards()
                            .stream()
                            .filter(findBoard -> !findBoard.getImageUrl().isEmpty())
                            .forEach(findBoard -> amazonService.deleteImage(findBoard.getImageUrl()));

                    userRepository.deleteById(findUser.getId());

                    return findUser;
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(NOT_FOUND_MESSAGE_FORMAT, "user")));
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(NOT_FOUND_MESSAGE_FORMAT, "user")));
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(NOT_FOUND_MESSAGE_FORMAT, "user")));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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

}

package com.springboot.blog.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.springboot.blog.controller.rest.UserRestController;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    @Value("${cloud.aws.s3.defaultImage}")
    private String defaultImage;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.amazonS3 = amazonS3;
    }

    @Transactional
    public ResponseEntity<String> signup(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다.");

        newUser.setRole(UserRole.ROLE_USER);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setPhotoUrl(defaultImage);

        Long id = userRepository.save(newUser).getId();

        return ResponseEntity.created(linkTo(methodOn(UserRestController.class)
                .signup(newUser)).slash(id).withSelfRel().toUri()).body("{}");
    }

    @Transactional
    public ResponseEntity<String> modify(Long id, User newUser) {
        return userRepository.findById(id).map(user -> {
            user.setIntroduction(newUser.getIntroduction());
            return ResponseEntity.ok("{}");
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseEntity<String> deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        deletePhoto(user.getPhotoUrl());
        user.getBoards().forEach(board -> deleteThumbnail(board.getThumbnailUrl()));
        userRepository.deleteById(id);

        return ResponseEntity.ok("{}");
    }

    @Transactional
    public ResponseEntity<String> modifyUserPhoto(Long id, MultipartFile newPhoto) {
        return userRepository.findById(id).map(user -> {
            deletePhoto(user.getPhotoUrl());
            String photoUrl = putPhoto(user, newPhoto);

            return ResponseEntity.ok(photoUrl);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseEntity<String> deleteUserPhoto(Long id) {
        return userRepository.findById(id).map(user -> {
            deletePhoto(user.getPhotoUrl());
            user.setPhotoUrl(defaultImage);
            return ResponseEntity.ok(defaultImage);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일을 확인해주세요."));
    }

    private String putPhoto(User user, MultipartFile photo) {
        if (photo != null) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());
            metadata.setContentLength(photo.getSize());

            try {
                String photoName = String.format("images/%s-%s", UUID.randomUUID(), photo.getOriginalFilename());
                user.setPhotoUrl(String.format("%s%s", url, photoName));
                amazonS3.putObject(new PutObjectRequest(bucket, photoName, photo.getInputStream(), metadata));
            } catch (Exception e) {
                throw new IllegalArgumentException("프로필 사진 저장에 실패했습니다.");
            }
        } else {
            user.setPhotoUrl(defaultImage);
        }

        return user.getPhotoUrl();
    }

    private void deletePhoto(String photo) {
        try {
            if (!photo.equals(defaultImage))
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, photo.replace(url, "")));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private void deleteThumbnail(String thumbnail) {
        try {
            if (!thumbnail.equals(defaultImage))
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, thumbnail.replace(url, "")));
        } catch (Exception e) {
            throw new IllegalArgumentException("파일 삭제에 실패했습니다.");
        }
    }

}
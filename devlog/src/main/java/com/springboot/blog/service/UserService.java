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

    @Value("${cloud.aws.s3.objectUrl}")
    private String objectUrl;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.amazonS3 = amazonS3;
    }

    @Transactional
    public ResponseEntity<String> modify(Long id, User newUser, MultipartFile newPhoto) {
        return userRepository.findById(id).map(user -> {
            user.setIntroduction(newUser.getIntroduction());

            if (newPhoto != null) {
                if (newUser.getPhotoUrl() != null) {
                    deletePhoto(newUser.getPhotoUrl());
                }
                String newPhotoName = String.format("images/%s-%s", UUID.randomUUID(), newPhoto.getOriginalFilename());
                putPhoto(newPhoto, newPhotoName);
                user.setPhotoUrl(String.format("%s%s", objectUrl, newPhotoName));
            }

            return ResponseEntity.ok("{}");
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseEntity<String> signup(User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다.");

        newUser.setRole(UserRole.ROLE_USER);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Long id = userRepository.save(newUser).getId();

        return ResponseEntity.created(linkTo(methodOn(UserRestController.class)
                .signup(newUser)).slash(id).withSelfRel().toUri()).body("{}");
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("이메일을 확인해주세요."));
    }

    private void putPhoto(MultipartFile photo, String photoName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(photo.getContentType());
        metadata.setContentLength(photo.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucket, photoName, photo.getInputStream(), metadata));
        } catch (Exception e) {
            throw new IllegalArgumentException("프로필 사진 저장에 실패했습니다.");
        }

    }

    private void deletePhoto(String photo) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, photo.replace(objectUrl, "")));
        } catch (Exception e) {
            throw new IllegalArgumentException("프로필 사진 삭제에 실패했습니다.");
        }
    }

}
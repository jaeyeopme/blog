package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping(value = "/users")
@RestController
public class UserRestController {

    private final String NULL_MESSAGE = "%s cannot be <strong>null</strong>.";
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<String> join(@RequestBody User newUser) {
//        userService.join(formValidation(newUser));
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

//    @PutMapping(value = "/{id}")
//    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody User newUser, @RequestPart MultipartFile newPhoto) {
//        userService.modify(id, formValidation(newUser), newPhoto);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> delete(@PathVariable Long id) {
//        userService.delete(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

//    /**
//     * 입력 값 유효성 검사
//     *
//     * @param newUser
//     * @return
//     */
//    private User formValidation(User newUser) {
//        if (newUser.getUsername().replaceAll(" ", "").isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE, "Username"));
//        }
//
//        if (newUser.getPassword().replaceAll(" ", "").isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE, "Password"));
//        }
//
//        if (newUser.getName().trim().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE, "Name"));
//        }
//
//        return newUser;
//    }

}

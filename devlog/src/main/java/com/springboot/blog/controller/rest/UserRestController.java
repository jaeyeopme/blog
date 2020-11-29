package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // TODO: 11/29/2020 REST HATEOAS link 수정 해야함
    @PostMapping(value = {"/users", "/join"})
    public ResponseEntity join(@RequestBody User newUser) {
        User user = userService.join(formValidation(newUser));

        Link selfRel = linkTo("/users")
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody User newUser, @RequestPart MultipartFile newPhoto) {
        User user = userService.modify(id, formValidation(newUser), newPhoto);

        Link selfRel = linkTo("/users")
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }


    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 입력 값 유효성 검사
     *
     * @param newUser
     * @return
     */
    private User formValidation(User newUser) {
        if (newUser.getUsername().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be <strong>null</strong>.");
        }

        if (newUser.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be <strong>null</strong>.");
        }

//        if (newUser.getEmail().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be <strong>null</strong>.");
//        }

        if (newUser.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be <strong>null</strong>.");
        }

        return newUser;
    }

}

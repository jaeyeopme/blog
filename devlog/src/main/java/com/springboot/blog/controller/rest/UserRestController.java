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

import static com.springboot.blog.controller.rest.UserRestController.BASE_URL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = UserRestController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class UserRestController {

    public static final String BASE_URL = "/users";
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 가입
     *
     * @param newUser
     * @return
     */
    @PostMapping
    public ResponseEntity join(@RequestBody User newUser) {
        User user = userService.join(formValidation(newUser));

        Link selfRel = linkTo(this.getClass())
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    /**
     * 회원 수정
     *
     * @param id
     * @param newUser
     * @param newPhoto
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody User newUser, @RequestPart MultipartFile newPhoto) {
        User user = userService.modify(id, formValidation(newUser), newPhoto);

        Link selfRel = linkTo(this.getClass())
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    /**
     * 회원 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 회원 유효성 검사
     *
     * @param newUser
     * @return
     */
    private User formValidation(User newUser) {
        if (newUser.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be <strong>null</strong>.");
        }
        if (newUser.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be <strong>null</strong>.");
        }
        return newUser;
    }

}

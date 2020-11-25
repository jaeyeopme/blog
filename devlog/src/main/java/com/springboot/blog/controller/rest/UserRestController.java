package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.springboot.blog.controller.rest.UserRestController.BASE_URL;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class UserRestController {

    public static final String BASE_URL = "/api/users";
    private final UserService userService;

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
        User user = userService.join(newUser);

        Link selfRel = linkTo(this.getClass())
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity modify(@PathVariable Long id, @RequestBody User newUser, @RequestPart MultipartFile newPhoto) {
        User user = userService.modify(id, newUser, newPhoto);

        Link selfRel = linkTo(this.getClass())
                .slash(user.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @PutMapping(value = "/photo/{id}")
//    public ResponseEntity<String> modify(@PathVariable Long id, @RequestPart MultipartFile photo) {
//        return userService.modifyUserPhoto(id, photo);
//    }
//
//    @DeleteMapping(value = "/photo/{id}")
//    public ResponseEntity<String> modify(@PathVariable Long id) {
//        return userService.deleteUserPhoto(id);
//    }

}

package com.springboot.blog.service;

import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.repository.UserRepository;
import netscape.security.ForbiddenTargetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.management.InstanceAlreadyExistsException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(User user) {
        try {
            user.setRole(UserRole.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("server error");
        }

        HttpStatus created = HttpStatus.CREATED;
        ApiResponse success = new ApiResponse(created, "Welcome to Blog", System.currentTimeMillis());


        return new ResponseEntity<>(success, created);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> validationEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException
                    ("Email " + email + " is already taken");
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "Email " + email + " is available", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Override // set spring security session (login)
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email " + email + " is not found"));
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(String.valueOf(user.getRole())));
//        authorities.add(() -> String.valueOf(user.getRole()));
        return user;
    }

}

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//            return new PrincipalDetail(user); // user 자체를 품고 있음
//            UserDetails userDetails = new UserDetails() {
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//                grantedAuthorities.add(new SimpleGrantedAuthority(String.valueOf(user.getRole())));
//                return grantedAuthorities;
//            }
//
//            @Override
//            public String getPassword() {
//                return user.getPassword();
//            }
//
//            // unique
//            @Override
//            public String getUsername() {
//                return user.getEmail();
//            }
//
//            @Override
//            public boolean isAccountNonExpired() {
//                return true;
//            }
//
//            @Override
//            public boolean isAccountNonLocked() {
//                return true;
//            }
//
//            @Override
//            public boolean isCredentialsNonExpired() {
//                return true;
//            }
//
//            @Override
//            public boolean isEnabled() {
//                return true;
//            }
//        };
//    @Transactional(readOnly = true) // 정합성
//    public ResponseEntity<ApiResponse> login(User user, HttpSession httpSession) { // HttpSession -> AUTO DI
//        User principal = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
//                .orElseThrow(() -> new RuntimeException("Incorrect email or password."));
//
//        httpSession.setAttribute("principal", principal);
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }
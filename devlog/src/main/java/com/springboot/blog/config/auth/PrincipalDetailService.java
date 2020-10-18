//package com.springboot.blog.config.auth;
//
//import com.springboot.blog.entity.User;
//import com.springboot.blog.repository.UserRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Service
//public class PrincipalDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public PrincipalDetailService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // validate username
//    // login()
//    // UserDetails 타입으로 로그인 해야 함
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email " + email + " is not found"));
//
//
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() ->
//                String.valueOf(user.getRole())
//        );
//
////         spring security User 클래스는 UserDetails 를 상속 (util class)
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
//    }
//
//}

package com.springboot.blog.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // username 확인 해야 됌 / password 는 spring security 내부 (등록된 password encoder 로 decode 해서 인증)에서 처리
    // return 해준 UserDetails 객체를 spring security holder 에 등록 @AuthenticationPrincipal 접근 가능
    // 로그인
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

    // Oauth2User ID와 DB에 있는 User Provider Id 의 정보가 일치하면 반환 받은 유저를 Oauth2User 타입으로 return
    // return 해준 OAuth2User 객체를 spring security holder 에 등록 @AuthenticationPrincipal 접근 가능
    // 회원가입 & 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        return null;
    }
}

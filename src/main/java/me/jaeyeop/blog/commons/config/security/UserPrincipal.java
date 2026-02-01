package me.jaeyeop.blog.commons.config.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import me.jaeyeop.blog.user.domain.Role;

public record UserPrincipal(Long id, Collection<? extends GrantedAuthority> authorities)
        implements OAuth2User {
    public UserPrincipal(final Long id, final Set<Role> roles) {
        this(id, roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}

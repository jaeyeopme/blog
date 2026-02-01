package me.jaeyeop.blog.authentication.domain;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import me.jaeyeop.blog.user.domain.Role;

public record TokenClaims(Long id, Collection<? extends GrantedAuthority> authorities) {
    public TokenClaims(final Long id, final Set<Role> authorities) {
        this(
                id,
                authorities.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList());
    }
}

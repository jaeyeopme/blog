package me.jaeyeop.blog.authentication.adapter.out;

import org.springframework.data.repository.CrudRepository;

import me.jaeyeop.blog.authentication.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {}

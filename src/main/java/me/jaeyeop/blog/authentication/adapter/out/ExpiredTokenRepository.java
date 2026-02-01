package me.jaeyeop.blog.authentication.adapter.out;

import org.springframework.data.repository.CrudRepository;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {}

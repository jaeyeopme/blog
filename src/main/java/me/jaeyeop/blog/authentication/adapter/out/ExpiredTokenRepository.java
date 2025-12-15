package me.jaeyeop.blog.authentication.adapter.out;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {}

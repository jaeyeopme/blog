package me.jaeyeop.blog.token.adapter.out;

import me.jaeyeop.blog.token.domain.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {

}

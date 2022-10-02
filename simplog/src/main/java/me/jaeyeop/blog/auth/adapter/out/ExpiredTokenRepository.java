package me.jaeyeop.blog.auth.adapter.out;

import me.jaeyeop.blog.auth.domain.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {

}

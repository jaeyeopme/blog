package me.jaeyeop.blog.token.adapter.out;

import me.jaeyeop.blog.token.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

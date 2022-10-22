package me.jaeyeop.blog.auth.adapter.out;

import me.jaeyeop.blog.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

package me.jaeyeop.blog.authentication.adapter.out;

import me.jaeyeop.blog.authentication.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

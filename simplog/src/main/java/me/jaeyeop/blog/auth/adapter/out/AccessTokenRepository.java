package me.jaeyeop.blog.auth.adapter.out;

import me.jaeyeop.blog.auth.domain.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

}

package me.jaeyeop.blog.authentication.adapter.out;

import me.jaeyeop.blog.authentication.domain.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

}

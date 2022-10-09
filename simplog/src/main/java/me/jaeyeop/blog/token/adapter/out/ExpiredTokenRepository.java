package me.jaeyeop.blog.token.adapter.out;

import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {

}

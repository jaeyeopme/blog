package me.jaeyeop.blog.authentication.adapter.out;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {}

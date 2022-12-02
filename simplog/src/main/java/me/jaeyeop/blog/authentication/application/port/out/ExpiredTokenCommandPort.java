package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public interface ExpiredTokenCommandPort {

  void expire(ExpiredToken token);

}

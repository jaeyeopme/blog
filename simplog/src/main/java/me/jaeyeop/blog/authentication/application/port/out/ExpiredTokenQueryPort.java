package me.jaeyeop.blog.authentication.application.port.out;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public interface ExpiredTokenQueryPort {

  boolean isExpired(String token);

}

package me.jaeyeop.blog.authentication.application.port.in;

import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Logout;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Refresh;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public interface AuthenticationCommandUseCase {

  void logout(Logout request);

  String refresh(Refresh request);

}

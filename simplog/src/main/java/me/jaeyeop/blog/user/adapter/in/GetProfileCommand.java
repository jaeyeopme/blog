package me.jaeyeop.blog.user.adapter.in;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class GetProfileCommand {

  private final String email;

}

package me.jaeyeop.blog.config.web;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebConfig {

  @Bean
  public LocaleResolver localeResolver() {
    final var localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.KOREA);
    return localeResolver;
  }

}

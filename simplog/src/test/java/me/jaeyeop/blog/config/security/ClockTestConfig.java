package me.jaeyeop.blog.config.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "me.jaeyeop.blog.config.clock")
public class ClockTestConfig {

}

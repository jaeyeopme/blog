package me.jaeyeop.blog.commons.config.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.jaeyeop.blog.commons.token.TokenType;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtractToken {
    @AliasFor("type")
    TokenType value() default TokenType.ACCESS;

    @AliasFor("value")
    TokenType type() default TokenType.ACCESS;
}

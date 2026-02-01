package me.jaeyeop.blog.commons.config.jpa;

import jakarta.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDSLConfig {
    @Bean
    public JPAQueryFactory jpaQueryFactory(final EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}

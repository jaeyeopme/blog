package me.jaeyeop.blog.config.openapi;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import me.jaeyeop.blog.auth.adapter.in.AuthWebAdaptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;

@Profile("dev")
@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(getInfo())
        .components(getComponents());
  }

  private Info getInfo() {
    return new Info()
        .title("Blog API Documentation")
        .description("""
            Blog 프로젝트 API 문서 입니다.<br/> <br/>
            권한이 필요한 요청에는 <b>엑세스 토큰</b>이 필요합니다.<br/>
            <a href="http://localhost:8080%s" target="_blank">소셜 인증</a>
            을 통해 발급한 <b>엑세스 토큰</b>을 우측의 <code>Authorize</code> 버튼을 눌러 등록해주세요.
            """.formatted(AuthWebAdaptor.AUTH_API_URI + "/login/google"))
        .contact(new Contact()
            .name("jaeyeopme")
            .email("jaeyeopme@gmail.com")
            .url("https://github.com/jaeyeopme/blog"))
        .version("v1");
  }

  private Components getComponents() {
    return new Components()
        .addSecuritySchemes("access_token", new SecurityScheme()
            .type(HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(HEADER)
            .name(HttpHeaders.AUTHORIZATION)
            .description("인가에 필요한 엑세스 토큰"));
  }

}

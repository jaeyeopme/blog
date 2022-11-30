package me.jaeyeop.blog.config.oas;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;

@Profile(value = {"local", "dev"})
@Configuration
public class OASConfig {

  private final String serverHost;

  private final long serverPort;

  private final String contactName;

  private final String contactEmail;

  private final String contactUrl;

  public OASConfig(
      @Value("${oas.server.host}") final String serverHost,
      @Value("${oas.server.port}") final long serverPort,
      @Value("${oas.contact.name}") final String contactName,
      @Value("${oas.contact.email}") final String contactEmail,
      @Value("${oas.contact.url}") final String contactUrl) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
    this.contactName = contactName;
    this.contactEmail = contactEmail;
    this.contactUrl = contactUrl;
  }

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
            권한이 필요한(자물쇠 그림) 요청에는 <b>엑세스 토큰</b>이 필요합니다.<br/>
            <a href="%s:%d%s" target="_blank">소셜 인증</a>을
             통해 발급한 <b>엑세스 토큰</b>을 우측의 <code>Authorize</code> 버튼을 눌러 등록해주세요.
            """.formatted(serverHost, serverPort,
            AuthenticationWebAdaptor.AUTHENTICATION_API_URI + "/google"))
        .contact(new Contact()
            .name(contactName)
            .email(contactEmail)
            .url(contactUrl))
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

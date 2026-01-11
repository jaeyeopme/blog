package me.jaeyeop.blog.commons.config.oas;

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
    private final String contactName;
    private final String contactEmail;
    private final String contactUrl;

    public OASConfig(
            @Value("${oas.contact.name}") final String contactName,
            @Value("${oas.contact.email}") final String contactEmail,
            @Value("${oas.contact.url}") final String contactUrl) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactUrl = contactUrl;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(getInfo()).components(getComponents());
    }

    private Info getInfo() {
        return new Info()
                .title("Blog API Documentation")
                .description(
                        """
                        <p>Blog 프로젝트 API 문서 입니다.</p>
                        <p><b>🔒권한</b>이 필요한 요청은 <b>accessToken</b>이 필요합니다.</p>
                        <p>
                          <a href="%s" target="_blank">소셜 인증</a>으로
                          <b>회원가입</b> 또는 <b>로그인</b>을 통해 발급한 <b>accessToken</b>을
                          우측의 <b>Authorize</b>에 등록해주세요.
                        </p>
                        <p>
                          <b>accessToken</b>이 만료되었다면, 발급한 <b>refreshToken</b>을 통해
                          <a target="_blank" href="#/1.authentication/refresh">재발급</a>해주세요.
                        </p>
                        """
                                .formatted(
                                        AuthenticationWebAdaptor.AUTHENTICATION_API_URI
                                                + "/login/google"))
                .contact(new Contact().name(contactName).email(contactEmail).url(contactUrl))
                .version("v1");
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes(
                        "access_token",
                        new SecurityScheme()
                                .type(HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(HEADER)
                                .name(HttpHeaders.AUTHORIZATION)
                                .description("인가에 필요한 엑세스 토큰"));
    }
}

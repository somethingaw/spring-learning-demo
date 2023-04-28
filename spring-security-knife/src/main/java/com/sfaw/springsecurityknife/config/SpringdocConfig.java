package com.sfaw.springsecurityknife.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringdocConfig
 * springdoc 配置
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/17 20:41
 **/

@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SpringdocConfig {

    public static final String AUTH_SCHEMA_NAME = "oauth";

    @Bean
    @ConfigurationProperties(prefix = "springdoc.api-docs.info")
    public Info springDocInfo() {
        return new Info();
    }

    @Bean
    public OpenAPI openAPI(Info infoConfig) {
        return new OpenAPI()
                .paths(paths())
                .components(oauthComponents())
                .info(infoConfig)
                .addSecurityItem(oauthSecurityRequirement())
                .externalDocs(new ExternalDocumentation()
                        .description("user md文档")
                        .url("/static/docs/user.md"));
    }

    public Paths paths() {
        return new Paths()
                .addPathItem("/user/update", new PathItem().post(
                        new Operation()
                                .description("[这是一个md链接](/docs/user.md)")
                                .summary("测试 user update")));
    }

    /**************************************** oauth2 + security ************************************************/
    private Components oauthComponents() {
        return new Components()
                .addSecuritySchemes(AUTH_SCHEMA_NAME, new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .in(SecurityScheme.In.HEADER)
                        .description("oauth2+security")
                        .scheme("bearer")
                        .flows(new OAuthFlows()
                                .authorizationCode(
                                        new OAuthFlow()
                                                // 简化模式
                                                //.authorizationUrl("http://127.0.0.1:10005/auth/oauth/implicit/authorize")
                                                // 密码模式
                                                // .tokenUrl("http://127.0.0.1:10005/auth/oauth/password/token")
                                                // 授权码，做了简单的接口实现，并没有进行实质校验
                                                .authorizationUrl("http://127.0.0.1:10005/auth/oauth/authcode/authorize")
                                                .tokenUrl("http://127.0.0.1:10005/auth/oauth/authcode/token")
                                                // 客户端模式
                                                // .tokenUrl("http://127.0.0.1:10005/auth/oauth/client/token")
                                                // 刷新token
                                                .refreshUrl("http://127.0.0.1:10005/auth/oauth/refresh/token")
                                                .scopes(new Scopes()
                                                        .addString("read", "read")
                                                        .addString("update", "update")
                                                        .addString("delete", "delete"))
                                )));

    }

    private SecurityRequirement oauthSecurityRequirement() {
        return new SecurityRequirement().addList(AUTH_SCHEMA_NAME);
    }

    /**************************************** bearer auth ************************************************/

    private Components bearerComponents() {
        return new Components()
                .addSecuritySchemes("bearer", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .bearerFormat("JWT")

                );
    }

    private SecurityRequirement bearerSecurityRequirement() {
        return new SecurityRequirement().addList("bearer", "bearer");
    }


}

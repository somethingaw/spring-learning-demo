package com.sfaw.springsecurityknife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/24 15:32
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 为了页面展示markdown文档时需要
        configurer.mediaType("md", MediaType.TEXT_MARKDOWN);
    }
}

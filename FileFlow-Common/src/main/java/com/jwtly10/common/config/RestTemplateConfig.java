package com.jwtly10.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        // This is to allow support for PATCH requests
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}

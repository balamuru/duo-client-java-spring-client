package com.vbg.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DuoConfiguration {


    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

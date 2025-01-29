package com.transaction_service.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class FeignConfig {

    @Bean
    @Order(1)
    public RequestInterceptor feignClientInterceptor() {
        return new FeignClientInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public Logger.Level logging() {
        return Logger.Level.BASIC;
    }
}


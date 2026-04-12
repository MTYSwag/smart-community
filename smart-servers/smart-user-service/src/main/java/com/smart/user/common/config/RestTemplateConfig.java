package com.smart.user.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @description: RestTemplate 配置类
 */
@Configuration
public class RestTemplateConfig {

    @LoadBalanced // 负载均衡开启
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

package com.smart.community.gateway;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class SmartGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartGatewayApplication.class, args);
        log.info("网关启动成功");
    }
}

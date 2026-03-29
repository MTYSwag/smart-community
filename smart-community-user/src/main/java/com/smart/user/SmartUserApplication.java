package com.smart.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
/**
 * 用户启动类
 */
@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册发现
@Slf4j
public class SmartUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartUserApplication.class, args);
        log.info("用户服务启动成功");
    }
}

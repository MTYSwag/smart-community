package com.smart.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户启动类
 */
@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册发现
@Slf4j
@ComponentScan({"com.smart.user", "com.smart.common"})// 扫描其他模块的包
public class SmartUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartUserApplication.class, args);
        log.info("用户服务启动成功");
    }
}

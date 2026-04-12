package com.smart.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@ComponentScan({"com.smart.file", "com.smart.common"})  // 添加组件扫描
public class SmartFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartFileApplication.class, args);
        log.info("文件服务启动成功");
    }

}

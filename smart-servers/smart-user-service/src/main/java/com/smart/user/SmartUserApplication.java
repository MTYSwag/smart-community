package com.smart.user;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    //项目启动就监听nacos配置变化
    //发生变化的配置会自动更新到内存中
    //发送配置变化通知给所有监听者
    /**
     * Spring Boot 3.x 配置监听器
     * 使用 @RefreshScope 和 @ConfigurationProperties 实现配置热更新
     */
    @Bean
    ApplicationRunner runner(NacosConfigManager configManager) {
        return args -> {
            try {
                ConfigService configService = configManager.getConfigService();

                // 监听配置变化（Spring Boot 3.x 推荐方式）
                configService.addListener("smart-user-dev.yaml", "user", new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return Executors.newFixedThreadPool(2);
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        log.info("Nacos配置已更新：{}", configInfo);
                        // Spring Boot 3.x 会自动刷新 @ConfigurationProperties 和 @Value 注解的配置
                        // 无需手动处理，配置会自动生效
                    }
                });

                log.info("Nacos配置监听器已启动 - DataId: smart-user-dev.yaml, Group: user, Namespace: dev");

            } catch (Exception e) {
                log.warn("Nacos配置监听器启动失败（不影响应用启动）：{}", e.getMessage());
            }
        };
    }
}

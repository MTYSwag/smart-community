package com.smart.file.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置类
 */
@Configuration
public class SwaggerConfig {
    /**
     * 配置Swagger文档信息 + 全局安全认证
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // ================== 1. 基础文档信息 ==================
                .info(new Info()
                        .title("smart-community-file 接口文档")
                        .description("文件模块接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("孟天韵")
                                .email("xpcmty@icloud.com")
                        )
                );

    }
}
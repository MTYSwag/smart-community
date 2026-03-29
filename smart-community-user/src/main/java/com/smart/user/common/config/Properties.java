package com.smart.user.common.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 - 用户服务专用
 */
@Data
@Configuration
public class Properties {

    @Schema(description = "密码加密方式")
    @Value("${app.security.password-encoder}")
    private String passwordEncoder;

    @Schema(description = "bcrypt强度因子")
    @Value("${app.security.bcrypt-strength}")
    private int bcryptStrength;


}

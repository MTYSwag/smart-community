package com.smart.user.common.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "app")//批量绑定属性，前缀 为 user，无须@RefreshScope就能实现自动刷新
public class UserProperties {

    @Schema(description = "密码加密方式")
    @Value("${app.security.password-encoder}")
    private String passwordEncoder;

    @Schema(description = "bcrypt强度因子")
    @Value("${app.security.bcrypt-strength}")
    private int bcryptStrength;
}

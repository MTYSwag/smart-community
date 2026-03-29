package com.smart.user.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码加密配置类(将其加入到spring容器中)
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private Properties properties;

    @Bean
    public PasswordEncoder passwordEncoderOld() {
        // 设置强度因子（默认10，企业常用12-15）
        int strength = 12; // 更高的安全性，但计算时间更长
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 支持多种编码方式，便于密码迁移
        String encodingId = properties.getPasswordEncoder();
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder(properties.getBcryptStrength()));

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }
}

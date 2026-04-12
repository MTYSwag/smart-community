package com.smart.file.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置类，绑定application.yml中的minio配置
 */
@Configuration
@ConfigurationProperties(prefix = "minio") // 绑定minio配置
@Data
public class MinioConfig {
    /**
     * MinIO服务地址
     */
    private String endpoint;
    /**
     * 账号
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 默认桶名
     */
    private String bucketName;
    /**
     * 预览链接过期时间
     */
    private Integer previewExpire;

    /**
     * 注入MinioClient客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
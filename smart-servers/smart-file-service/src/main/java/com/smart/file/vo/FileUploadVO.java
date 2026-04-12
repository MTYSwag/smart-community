package com.smart.file.vo;

import lombok.Data;

/**
 * 文件上传返回结果
 */
@Data
public class FileUploadVO {
    /**
     * 文件唯一标识（MD5）
     */
    private String fileMd5;
    /**
     * 文件原名
     */
    private String originalFileName;
    /**
     * 文件访问路径（MinIO预览链接）
     */
    private String fileUrl;
    /**
     * 文件大小，单位字节
     */
    private Long fileSize;
}
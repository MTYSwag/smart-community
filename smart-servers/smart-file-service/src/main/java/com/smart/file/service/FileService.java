package com.smart.file.service;

import com.smart.file.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {
    /**
     * 单文件上传
     * @param file 上传的文件
     * @return 上传结果
     */
    FileUploadVO upload(MultipartFile file);

    /**
     * 根据文件MD5删除文件
     * @param fileMd5 文件MD5
     * @return 是否删除成功
     */
    Boolean deleteByMd5(String fileMd5);

    /**
     * 根据文件MD5获取预览链接
     * @param fileMd5 文件MD5
     * @return 预览链接
     */
    String getPreviewUrl(String fileMd5);
}
package com.smart.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.smart.file.config.MinioConfig;
import com.smart.file.service.FileService;
import com.smart.file.vo.FileUploadVO;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 文件服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor // 构造器注入，替代@Resource
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public FileUploadVO upload(MultipartFile file) {
        String originalFileName = null;
        InputStream inputStream = null;

        try {
            // 0. 校验文件是否为空
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("上传文件不能为空");
            }

            // 1. 获取文件基础信息
            originalFileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            inputStream = file.getInputStream();

            log.info("开始上传文件：{}，大小：{}字节", originalFileName, fileSize);

            // 2. 校验MinIO桶是否存在，不存在则创建
            String bucketName = minioConfig.getBucketName();
            log.info("检查MinIO桶是否存在：{}", bucketName);

            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                log.info("MinIO桶{}不存在，开始创建", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO桶{}创建成功", bucketName);
            } else {
                log.info("MinIO桶{}已存在", bucketName);
            }

            // 3. 生成文件MD5（唯一标识，用于秒传/删除/查询）
            log.info("生成文件MD5...");
            String fileMd5 = MD5.create().digestHex(inputStream);
            log.info("文件MD5生成成功：{}", fileMd5);

            // 4. 生成MinIO存储的文件名（MD5+后缀，避免重名）
            String fileSuffix = FileUtil.extName(originalFileName);
            String storeFileName = fileMd5 + "." + fileSuffix;
            log.info("存储文件名：{}", storeFileName);

            // 5. 上传文件到MinIO
            InputStream uploadStream = file.getInputStream();
            log.info("开始上传文件到MinIO...");
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(storeFileName) // 存储的文件名
                            .stream(uploadStream, fileSize, -1) // -1表示自动识别文件大小
                            .contentType(file.getContentType()) // 文件类型
                            .build()
            );
            uploadStream.close();
            log.info("文件{}上传成功，MD5：{}", originalFileName, fileMd5);

            // 6. 生成预览链接
            String fileUrl = getPreviewUrl(fileMd5);
            log.info("文件预览链接生成成功：{}", fileUrl);

            // 7. 封装返回结果
            FileUploadVO vo = new FileUploadVO();
            vo.setFileMd5(fileMd5);
            vo.setOriginalFileName(originalFileName);
            vo.setFileUrl(fileUrl);
            vo.setFileSize(fileSize);

            log.info("文件上传流程完成");
            return vo;

        } catch (Exception e) {
            log.error("文件上传失败 - 文件名：{}", originalFileName, e);
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        } finally {
            // 确保流关闭
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("关闭文件流失败", e);
                }
            }
        }
    }

    @Override
    public Boolean deleteByMd5(String fileMd5) {
        try {
            // 遍历桶中文件，根据MD5匹配删除（简化版，后续可优化为数据库记录文件名）
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .prefix(fileMd5)
                            .build()
            );

            for (Result<Item> objectResult : objects) {
                try {
                    Item item = objectResult.get();
                    if (item != null) {
                        minioClient.removeObject(
                                RemoveObjectArgs.builder()
                                        .bucket(minioConfig.getBucketName())
                                        .object(item.objectName())
                                        .build()
                        );
                        log.info("删除文件：{}", item.objectName());
                    }
                } catch (Exception e) {
                    log.error("删除文件失败", e);
                    throw new RuntimeException("文件删除失败");
                }
            }

            log.info("文件MD5：{}删除成功", fileMd5);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new RuntimeException("文件删除失败：" + e.getMessage());
        }
    }

    @Override
    public String getPreviewUrl(String fileMd5) {
        try {
            // 根据MD5查询文件对象
            String storeFileName = null;
            for (Result<Item> item : minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .prefix(fileMd5)
                            .build()
            )) {
                storeFileName = item.get().objectName();
                break;
            }
            if (storeFileName == null) {
                throw new RuntimeException("文件MD5：" + fileMd5 + "不存在");
            }

            // 生成带过期时间的预览链接
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(storeFileName)
                            .expiry(minioConfig.getPreviewExpire())
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件预览链接失败", e);
            throw new RuntimeException("获取文件预览链接失败：" + e.getMessage());
        }
    }
}
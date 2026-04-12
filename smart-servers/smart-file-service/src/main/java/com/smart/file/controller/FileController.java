package com.smart.file.controller;

import com.smart.common.result.Result;
import com.smart.file.service.FileService;
import com.smart.file.vo.FileUploadVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口控制器
 * 所有接口需JWT鉴权，网关已做拦截
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件接口", description = "单文件上传/删除/预览")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "单文件上传", description = "支持图片/视频/普通文件，返回预览链接")
    public Result<FileUploadVO> upload(
            @Parameter(description = "上传的文件", required = true)
            @RequestParam("file") MultipartFile file) {
        FileUploadVO vo = fileService.upload(file);
        return Result.success(vo);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "根据MD5删除文件", description = "传入文件上传返回的MD5")
    public Result<Boolean> delete(
            @Parameter(description = "文件MD5", required = true)
            @RequestParam("fileMd5") String fileMd5) {
        Boolean result = fileService.deleteByMd5(fileMd5);
        return Result.success(result);
    }

    @GetMapping("/preview")
    @Operation(summary = "根据MD5获取预览链接", description = "链接有过期时间，默认3600秒")
    public Result<String> preview(
            @Parameter(description = "文件MD5", required = true)
            @RequestParam("fileMd5") String fileMd5) {
        String url = fileService.getPreviewUrl(fileMd5);
        return Result.success(url);
    }
}
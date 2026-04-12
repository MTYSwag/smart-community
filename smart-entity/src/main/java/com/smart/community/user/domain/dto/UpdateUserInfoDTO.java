package com.smart.community.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户基本信息修改DTO
 */
@Data
public class UpdateUserInfoDTO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "头像url")
    private String avatarUrl;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

}

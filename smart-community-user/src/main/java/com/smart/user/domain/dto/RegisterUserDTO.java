package com.smart.user.domain.dto;

/**
 * 注册用户DTO
 */
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserDTO {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;
}

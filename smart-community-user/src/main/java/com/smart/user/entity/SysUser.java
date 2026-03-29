package com.smart.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * sys_user 用户表实体类
 */
@Data
@TableName("sys_user")
@Accessors(chain = true)
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID) // 雪花算法生成ID
    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户名")
    private String nickname;

    @Schema(description = "头像url")
    private String avatarUrl;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态 1正常 0禁用")
    private Integer status; // 1正常 0禁用

    @Schema(description = "关注数")
    private Integer followCount;

    @Schema(description = "粉丝数")
    private Integer fansCount;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableLogic // 逻辑删除
    @Schema(description = "逻辑删除 1删除 0正常")
    private Integer deleted;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否逻辑删除 1删除 0正常")
    private Integer isDeleted;

}

package com.smart.community.user.domain.dto;


import com.smart.community.common.dto.PageQueryDTO;
import lombok.Data;

@Data
public class UserPageDTO extends PageQueryDTO {

    // 用户名模糊检索
    private String username;
}

package com.smart.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.user.domain.dto.UserPageDTO;
import com.smart.user.domain.vo.UserInfoVo;
import com.smart.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户表 Mapper 接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    // 分页列表
    List<UserInfoVo> selectUserPage(@Param("dto") UserPageDTO dto);
    // 总条数
    Long selectUserPageCount(@Param("dto") UserPageDTO dto);
}

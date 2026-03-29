package com.smart.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}

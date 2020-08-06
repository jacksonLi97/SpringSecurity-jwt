package com.lizhongjie.blog.mapper;

import com.lizhongjie.blog.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    SysUser findByUsername(@Param("username") String username);
}

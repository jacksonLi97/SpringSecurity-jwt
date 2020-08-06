package com.lizhongjie.blog.service;

import com.lizhongjie.blog.entity.SysUser;

public interface AuthService {
    SysUser register(SysUser userToAdd);
    String login(String username, String password);
}

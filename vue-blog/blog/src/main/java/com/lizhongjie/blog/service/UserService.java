package com.lizhongjie.blog.service;

import com.lizhongjie.blog.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SysUser findByUsername(String username);
}

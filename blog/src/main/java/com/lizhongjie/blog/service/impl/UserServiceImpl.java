package com.lizhongjie.blog.service.impl;

import com.lizhongjie.blog.entity.SysUser;
import com.lizhongjie.blog.mapper.UserMapper;
import com.lizhongjie.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = userMapper.findByUsername(s);
        if (sysUser==null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", s));
        } else {
            return sysUser;
        }
    }


    @Override
    public SysUser findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}

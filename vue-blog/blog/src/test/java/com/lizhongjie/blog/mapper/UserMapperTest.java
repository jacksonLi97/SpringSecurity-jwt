package com.lizhongjie.blog.mapper;

import com.lizhongjie.blog.BlogApplicationTests;
import com.lizhongjie.blog.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


class UserMapperTest extends BlogApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void findByUsername() {
        SysUser sysUser = userMapper.findByUsername("lzj");
        System.out.println(sysUser.getId()+sysUser.getUsername()+sysUser.getPassword());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Test
    void testBCryptPasswordEncoder(){
        String password = "123";
        System.out.println(passwordEncoder().encode(password));
    }
}
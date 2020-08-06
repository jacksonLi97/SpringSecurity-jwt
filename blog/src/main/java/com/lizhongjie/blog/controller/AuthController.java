package com.lizhongjie.blog.controller;

import com.lizhongjie.blog.entity.SysUser;
import com.lizhongjie.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String login( @RequestBody SysUser user) throws AuthenticationException{
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println(username + password);
        return authService.login(username, password);
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public String register() throws AuthenticationException{

        return "register";
    }
}

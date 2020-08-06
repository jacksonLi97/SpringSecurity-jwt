package com.lizhongjie.blog.service.impl;

import com.lizhongjie.blog.entity.Role;
import com.lizhongjie.blog.entity.SysUser;
import com.lizhongjie.blog.service.AuthService;
import com.lizhongjie.blog.service.UserService;
import com.lizhongjie.blog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @Override
    public SysUser register(SysUser userToAdd) {
        return null;
    }

    @Override
    public String login(String username, String password) {
//        return "login";
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SysUser sysUser = userService.findByUsername(username);
        String role = "";
        Collection<? extends GrantedAuthority> authorities = sysUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }
        System.out.println(role);
        //生成claim准备生成token
        Map claim = new HashMap();
        claim.put("username", sysUser.getUsername());
        claim.put("role", role);
        String token = "Bearer " + JwtTokenUtil.generateToken(claim);
        System.out.println("token:"+token);
        return token;
    }


}

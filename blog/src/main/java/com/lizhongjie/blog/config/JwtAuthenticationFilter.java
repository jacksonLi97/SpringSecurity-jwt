package com.lizhongjie.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhongjie.blog.entity.Role;
import com.lizhongjie.blog.entity.SysUser;
import com.lizhongjie.blog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证用户名密码，生成1个token返回给客户端
 * 继承了UsernamePasswordAuthenticationFilter类，重新两个方法
 * attemptAuthentication：接收并解析用户凭证
 * successfulAuthentication：用户成功登录后，这个方法会被调用，在这个方法里生成token并返回
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //改登录url
//        super.setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //从输入流获取登录信息
        try {
            SysUser sysUser = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);

//            SysUser sysUser = new SysUser();
//            sysUser.setUsername(request.getParameter("username"));
//            sysUser.setPassword(request.getParameter("password"));
            //获取user的username和password并进行验证
            //并调用authenticationManager.authenticate()让spring-security
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(sysUser.getUsername(),sysUser.getPassword()));
        }catch (Exception e){
            e.printStackTrace();
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                Map resultMap = new HashMap();
                resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg", "用户名或密码错误");
                out.write(new ObjectMapper().writeValueAsString(resultMap));
                out.flush();
                out.close();
            }catch (Exception outEx){
                outEx.printStackTrace();
            }
            throw new RuntimeException(e);
        }

    }

    //成功验证后调用的方法
    //如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SysUser sysUser = (SysUser) authResult.getPrincipal();
        sysUser.setRoles((List<Role>) authResult.getAuthorities());
        System.out.println("sysUser:"+sysUser.toString());
        //生成claim准备生成token
        Map claim = new HashMap();
        claim.put("username", sysUser.getUsername());
        claim.put("role", sysUser.getRoles());
        String token = JwtTokenUtil.generateToken(claim);
        System.out.println("token:"+token);
        //添加token到header
        response.setHeader("token", "Bearer " + token);
        //返回头部信息
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            Map resultMap = new HashMap();
            resultMap.put("code", HttpServletResponse.SC_OK);
            resultMap.put("msg", "认证成功");
            out.write(new ObjectMapper().writeValueAsString(resultMap));
            out.flush();
            out.close();
        }catch (Exception outEx){
            outEx.printStackTrace();
        }

    }
}

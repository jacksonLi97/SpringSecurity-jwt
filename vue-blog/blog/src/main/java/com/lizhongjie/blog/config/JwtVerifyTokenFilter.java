package com.lizhongjie.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhongjie.blog.utils.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证成功后进行鉴权，告诉Spring Security用户是否登录，是什么角色，拥有什么权限
 */
public class JwtVerifyTokenFilter extends BasicAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer "; // Token前缀
    public static final String HEADER_STRING = "token"; // 存放Token的Header Key

    public JwtVerifyTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_STRING);
        //验证token格式是否正确
        if (authHeader!=null && authHeader.startsWith(TOKEN_PREFIX)) {
            final String authToken = authHeader.replace(TOKEN_PREFIX, "");
            //从token中获取username和role
            String username = JwtTokenUtil.getUsernameFromToken(authToken);
            String role = JwtTokenUtil.getRoleFromToken(authToken);
//            System.out.println(username+role);
//            System.out.println(Collections.singleton(new SimpleGrantedAuthority(role)));
            if (username != null) {
                //验证token是否过期
                if (!JwtTokenUtil.isTokenExpired(authToken)) {
                    try {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                                Collections.singleton(new SimpleGrantedAuthority(role)));
//                        System.out.println(1);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        System.out.println(2);
                    }catch (Exception e){
                        e.toString();
                    }
                }
            }
        }
        chain.doFilter(request, response);
//        } else {
//            chain.doFilter(request,response);
//            response.setContentType("application/json;charset=utf-8");
//            response.setStatus(response.SC_FORBIDDEN);
//            PrintWriter out = response.getWriter();
//            Map resultMap = new HashMap();
//            resultMap.put("code", response.SC_FORBIDDEN);
//            resultMap.put("msg", "请登录");
//            out.write(new ObjectMapper().writeValueAsString(resultMap));
//            out.flush();
//            out.close();
//        }

    }
}

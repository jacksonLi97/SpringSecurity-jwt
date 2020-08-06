package com.lizhongjie.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private static String SECRET_KEY = "lzj123";
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJyaWQiOjEsInJvbGVOYW1lIjoiUk9MRV9VU0VSIiwicm9sZURlc2MiOiJVU0VSIiwiYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJleHAiOjE1OTY2MTU0NTcsImlhdCI6MTU5NjYxMTg1NywidXNlcm5hbWUiOiJsemoifQ.PNBPnyint2DFoMn0Y40lFwDA32SjLsna7JRkQPeB0ic";

    @Test
    void getClaimsFromToken() {
        System.out.println(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody());

    }

//    @Test
//    void getUsernameFromToken() {
//        String username;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            username = claims.get("username").toString();
//        } catch (Exception e) {
//            username = null;
//        }
//        return username;
//    }
//
    @Test
    void getRoleFromToken() {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims.get("role").toString());
    }
}
package com.lizhongjie.blog.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
@Data
public class Role implements GrantedAuthority {

    private Integer rid;
    private String roleName;
    private String roleDesc;

    @Override
    public String getAuthority() {
        return roleName;
    }
}

package com.sfaw.springsecurityknife.handler;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * MyPermissionEvaluator
 * 自定义 permission 校验接口
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/5/5 15:38
 **/
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject.toString().equals(authentication.getAuthorities().toString())) {
            // todo 校验用户角色
            if ("update".equals(permission.toString())) {
                // todo 校验具体权限
            }
        }
        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return true;
    }
}

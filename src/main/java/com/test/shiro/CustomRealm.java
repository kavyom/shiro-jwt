package com.test.shiro;

import com.test.model.SysUser;
import com.test.service.LoginService;
import com.test.util.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String username = JWTUtil.getUsername(principalCollection.toString());
        //用户权限列表
        Set<String> permsSet = loginService.getUserPermissions(username);
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();

        // 从token中获得username
        String username = JWTUtil.getUsername(token);
        // 如果username为空或者验证不匹配
        if(username == null || !JWTUtil.verify(token, username)){
            throw new AuthenticationException("token认证失败!");
        }

        SysUser sysUser = loginService.queryByUserName(username);
        // 用户是否存在
        if(sysUser == null){
            throw new AuthenticationException("该用户不存在");
        }
        // 账号状态
        if(sysUser.getStatus() == 1){
            throw new LockedAccountException("账号已被锁定");
        }

        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }
}

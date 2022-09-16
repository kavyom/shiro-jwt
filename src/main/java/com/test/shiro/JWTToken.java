package com.test.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by pzh on 2022/2/22.
 */
public class JWTToken implements AuthenticationToken {
    private static final long serialVersionUID = -2183012814134319396L;

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

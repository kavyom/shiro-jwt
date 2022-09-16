package com.test.shiro;

import com.google.gson.Gson;
import com.test.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pzh on 2022/2/22.
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            responseError(response, 10001, "token not found");
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String token = getToken(request);
        JWTToken jwtToken = new JWTToken(token);
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            responseError(response, 10002, "invalid token");
            return false;
        }
    }

    private String getToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return token == null ? null : token.replace("Bearer ","");
    }

    private void responseError(ServletResponse response, int code, String message) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String json = new Gson().toJson(BaseResult.error(code, message));
        try {
            httpResponse.getWriter().print(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 对跨域访问提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }
}

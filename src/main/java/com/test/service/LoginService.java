package com.test.service;

import com.test.model.SysUser;

import java.util.Set;

/**
 * Created by pzh on 2022/2/23.
 */
public interface LoginService {

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    SysUser queryByUserName(String userName);

    /**
     * 查询用户权限列表
     * @param userName
     * @return
     */
    Set<String> getUserPermissions(String userName);
}

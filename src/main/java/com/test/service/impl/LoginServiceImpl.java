package com.test.service.impl;

import com.test.mapper.LoginMapper;
import com.test.model.SysUser;
import com.test.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pzh on 2022/2/23.
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public SysUser queryByUserName(String userName) {
        return loginMapper.queryByUserName(userName);
    }

    @Override
    public Set<String> getUserPermissions(String userName) {
        List<String> permsList = loginMapper.queryAllPerms(userName);

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
}

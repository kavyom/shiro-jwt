package com.test.mapper;

import com.test.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pzh on 2022/2/23.
 */
@Mapper
@Repository
public interface LoginMapper {

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    SysUser queryByUserName(String userName);

    /**
     * 查询用户所有授权
     * @param userName
     * @return
     */
    List<String> queryAllPerms(String userName);
}

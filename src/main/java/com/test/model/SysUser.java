package com.test.model;

import com.test.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUser extends BaseEntity {

    private Long id;

    private String userName;

    private String password;

    private String nickName;

    private String salt;

    private String phone;

    private String email;

    private Integer status;
}

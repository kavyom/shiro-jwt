package com.test.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by pzh on 2022/3/16.
 */
@Data
public class LoginForm {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @NotBlank(message = "uuid不能为空")
    private String uuid;
}

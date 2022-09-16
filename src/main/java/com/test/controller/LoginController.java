package com.test.controller;

import com.google.code.kaptcha.Producer;
import com.test.base.BaseResult;
import com.test.exception.BusinessException;
import com.test.model.SysUser;
import com.test.service.LoginService;
import com.test.util.Base64Util;
import com.test.util.JWTUtil;
import com.test.util.RedisUtil;
import com.test.util.UUIDUtil;
import com.test.vo.LoginForm;
import com.test.vo.ValidateCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/sys")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private Producer producer;

	@Autowired
	private RedisUtil redisUtil;

	@PostMapping("/login")
	public BaseResult login(@Valid @RequestBody LoginForm loginForm) {
		String userName = loginForm.getUserName();
		String password = loginForm.getPassword();
		String captcha = loginForm.getCaptcha();
		String uuid = loginForm.getUuid();
		log.info("用户登录 userName:" + userName + " password:" + password + " captcha:" + captcha + " uuid:" + uuid);

		String kaptcha = getKaptcha(uuid);
		if (!captcha.equalsIgnoreCase(kaptcha)) {
			return BaseResult.error(1001, "验证码不正确");
		}

		// 用户信息
		SysUser sysUser = loginService.queryByUserName(userName);

		// 账号不存在、密码错误
		if (sysUser == null || !sysUser.getPassword().equals(new Sha256Hash(password, sysUser.getSalt()).toHex())) {
			return BaseResult.error(1002, "账号或密码不正确");
		}

		// 账号锁定
		if (sysUser.getStatus() == 1) {
			return BaseResult.error(1003, "账号已被锁定，请联系管理员");
		}

		String token = JWTUtil.createToken(userName);
		return BaseResult.success(token);
	}

	@GetMapping("/captcha")
	public BaseResult captcha() throws IOException {
		// 生成文字验证码
		String text = producer.createText();
		log.info("生成验证码:" + text);
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到redis
		String uuid = UUIDUtil.generate();
		redisUtil.set(uuid, text, 5*60);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		String base64Img = Base64Util.encode(out.toByteArray());

		ValidateCode validateCode = new ValidateCode();
		validateCode.setUuid(uuid);
		validateCode.setBase64Img(base64Img);
		return BaseResult.success(validateCode);
	}

	/**
	 * 获取验证码
	 * @param uuid
	 * @return
	 */
	private String getKaptcha(String uuid) {
		String kaptcha = redisUtil.get(uuid);
		if (kaptcha == null) {
			throw new BusinessException("验证码已失效");
		}
		redisUtil.delete(uuid);
		return kaptcha;
	}
}

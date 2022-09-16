package com.test.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by pzh on 2022/2/22.
 *
 * JWT工具类
 */
@Slf4j
public class JWTUtil {

    private static final long EXPIRE_TIME = 12*60*60*1000;
    private static final String SECRET = "ukc8BDbRigUDaY6pZFfWus2jWSYIKM";

    /**
     * 生成jwt token
     * @param username 用户名
     * @return token
     */
    public static String createToken(String username){
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 验证token是否正确
     * @param token 前端传过来的token
     * @param username 用户名
     * @return 返回boolean
     */
    public static boolean verify(String token, String username){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", username)
                .build();
        verifier.verify(token);
        return true;
    }

    /**
     * 从token中获得username，无需secret
     * @param token token
     * @return username
     */
    public static String getUsername(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }
}

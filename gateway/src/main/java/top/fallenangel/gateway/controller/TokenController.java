package top.fallenangel.gateway.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.confige.TokenConfig;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("token")
public class TokenController {
    private final TokenConfig tokenConfig;
    private final TokenUtil tokenUtil;
    private final StringRedisTemplate redisTemplate;

    public TokenController(TokenConfig tokenConfig, TokenUtil tokenUtil, StringRedisTemplate redisTemplate) {
        this.tokenConfig = tokenConfig;
        this.tokenUtil = tokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("refresh")
    public Result<Map<String, String>> refresh(@RequestBody Map<String, String> param, Authentication authentication) {
        String refreshToken = param.get("refreshToken");
        String username = authentication.getName();
        String refreshTokenKey = tokenUtil.getRedisKey(username, "refreshToken");
        String refreshTokenFromRedis = redisTemplate.opsForValue().get(refreshTokenKey);

        if (refreshTokenFromRedis == null) {
            return Result.create(ResponseCode.REFRESH_TOKEN_EXPIRED);
        }

        if (!refreshToken.equals(refreshTokenFromRedis)) {
            return Result.create(ResponseCode.REFRESH_TOKEN_INVALID);
        }

        String newRefreshToken = Util.uuid();
        int refreshTokenTimeout = tokenConfig.getRefreshTimeout();
        String newToken = tokenUtil.createToken((UserDetails) authentication.getDetails());

        Map<String, String> data = new HashMap<>();

        data.put("token", newToken);
        data.put("refreshToken", newRefreshToken);

        redisTemplate.opsForValue().set(tokenUtil.getRedisKey(username, "token"), newToken, refreshTokenTimeout, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(tokenUtil.getRedisKey(username, "refreshToken"), newRefreshToken, refreshTokenTimeout, TimeUnit.DAYS);

        return Result.ok(data);
    }
}

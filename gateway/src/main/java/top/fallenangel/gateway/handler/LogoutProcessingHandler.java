package top.fallenangel.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutProcessingHandler implements LogoutHandler {
    private final StringRedisTemplate redisTemplate;
    private final TokenUtil tokenUtil;

    public LogoutProcessingHandler(StringRedisTemplate redisTemplate, TokenUtil tokenUtil) {
        this.redisTemplate = redisTemplate;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        JSONObject bodyJson = JSON.parseObject(Util.readBodyJson(request));

        System.out.println("SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
        System.out.println("authentication = " + authentication);

        redisTemplate.delete(tokenUtil.getRedisKey(bodyJson.getString("username"), "token"));
        redisTemplate.delete(tokenUtil.getRedisKey(bodyJson.getString("username"), "refreshToken"));
    }
}

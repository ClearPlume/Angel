package top.fallenangel.gateway.handler;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutProcessingHandler implements LogoutHandler {
    private final StringRedisTemplate redisTemplate;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public LogoutProcessingHandler(StringRedisTemplate redisTemplate, TokenUtil tokenUtil, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();

        redisTemplate.delete(tokenUtil.getRedisKey(username, "token"));
        redisTemplate.delete(tokenUtil.getRedisKey(username, "refreshToken"));

        // userRepository.logout(username);
    }
}

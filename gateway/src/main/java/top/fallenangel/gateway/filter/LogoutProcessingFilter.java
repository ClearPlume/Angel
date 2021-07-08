package top.fallenangel.gateway.filter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.util.AuthenticationUtil;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutProcessingFilter extends LogoutFilter {
    private final AuthenticationUtil authenticationUtil;
    private final TokenUtil tokenUtil;
    private final StringRedisTemplate redisTemplate;

    public LogoutProcessingFilter(LogoutSuccessHandler logoutSuccessHandler, AuthenticationUtil authenticationUtil, TokenUtil tokenUtil, StringRedisTemplate redisTemplate, LogoutHandler... handlers) {
        super(logoutSuccessHandler, handlers);
        this.authenticationUtil = authenticationUtil;
        this.tokenUtil = tokenUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().equals("/logout")) {
            String token = request.getHeader(TokenUtil.TOKEN_HEADER);

            if (token == null) {
                Util.responseJson(response, Result.create(ResponseCode.UNAUTHORIZED));
                return;
            }

            boolean tokenExpired;
            try {
                String tokenKey = tokenUtil.getRedisKey(tokenUtil.getUsername(token), "token");
                String tokenFromRedis = redisTemplate.opsForValue().get(tokenKey);
                tokenExpired = tokenUtil.isExpiration(tokenFromRedis);
            } catch (NullPointerException e) {
                Util.responseJson(response, Result.create(ResponseCode.TOKEN_EXPIRED));
                return;
            } catch (Exception e) {
                Util.responseJson(response, Result.create(ResponseCode.TOKEN_INVALID));
                return;
            }

            if (tokenExpired) {
                Util.responseJson(response, Result.create(ResponseCode.TOKEN_EXPIRED));
                return;
            }

            SecurityContext securityContext = SecurityContextHolder.getContext();

            if (securityContext.getAuthentication() == null) {
                try {
                    securityContext.setAuthentication(authenticationUtil.authentication(token));
                } catch (Exception e) {
                    Util.responseJson(response, Result.create(ResponseCode.TOKEN_INVALID));
                    return;
                }
            }
        }

        super.doFilter(req, res, chain);
    }
}

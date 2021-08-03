package top.fallenangel.gateway.filter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.confige.AnonymousUriConfig;
import top.fallenangel.gateway.util.AuthenticationUtil;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private final TokenUtil tokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final StringRedisTemplate redisTemplate;
    private final AnonymousUriConfig anonymousUriConfig;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, TokenUtil tokenUtil, AuthenticationUtil authenticationUtil, StringRedisTemplate redisTemplate, AnonymousUriConfig anonymousUriConfig) {
        super(authenticationManager);
        this.tokenUtil = tokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.redisTemplate = redisTemplate;
        this.anonymousUriConfig = anonymousUriConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!anonymousUriConfig.isAnonymous(request)) {
            String token = request.getHeader(TokenUtil.TOKEN_HEADER);

            if (token == null) {
                Util.responseJson(response, Result.create(ResponseCode.UNAUTHORIZED));
                return;
            }

            if (!request.getRequestURI().equals("/token/refresh")) {
                boolean tokenExpired;
                String tokenFromRedis;
                try {
                    String tokenKey = tokenUtil.getRedisKey(tokenUtil.getUsername(token), "token");
                    tokenFromRedis = redisTemplate.opsForValue().get(tokenKey);
                    tokenExpired = tokenUtil.isExpiration(tokenFromRedis);
                } catch (NullPointerException e) {
                    Util.responseJson(response, Result.create(ResponseCode.TOKEN_EXPIRED));
                    return;
                } catch (Exception e) {
                    Util.responseJson(response, Result.create(ResponseCode.TOKEN_INVALID));
                    return;
                }

                if (!token.equals(tokenFromRedis)) {
                    Util.responseJson(response, Result.create(ResponseCode.TOKEN_INVALID));
                    return;
                }

                if (tokenExpired) {
                    Util.responseJson(response, Result.create(ResponseCode.TOKEN_EXPIRED));
                    return;
                }
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

        super.doFilterInternal(request, response, chain);
    }
}

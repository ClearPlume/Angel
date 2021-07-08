package top.fallenangel.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.confige.TokenConfig;
import top.fallenangel.gateway.dto.UserDTO;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 处理登录逻辑，包含以下内容：
 * <p/>
 * &emsp;&emsp;1. 拦截“GET /login”请求，从请求体获取用户信息
 * <br />
 * &emsp;&emsp;2. 登录成功后的逻辑
 * <br />
 * &emsp;&emsp;3. 登录失败后的逻辑
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final TokenConfig tokenConfig;
    private final StringRedisTemplate redisTemplate;

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenUtil tokenUtil, TokenConfig tokenConfig, StringRedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
        this.redisTemplate = redisTemplate;
        setAuthenticationManager(authenticationManager);

        this.tokenConfig = tokenConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            JSONObject bodyJson = JSON.parseObject(Util.readBodyJson(request));

            String username = bodyJson.getString(getUsernameParameter());
            String password = bodyJson.getString(getPasswordParameter());

            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
            authentication.setDetails(new UserDTO(userRepository.findByUsername(username)));

            return getAuthenticationManager().authenticate(authentication);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String token = tokenUtil.createToken((UserDTO) authResult.getPrincipal());

        String refreshToken = Util.uuid();
        int refreshTokenTimeout = tokenConfig.getRefreshTimeout();

        String username = authResult.getName();

        Map<String, String> data = new HashMap<>();

        data.put("token", token);
        data.put("refreshToken", refreshToken);

        redisTemplate.opsForValue().set(tokenUtil.getRedisKey(username, "token"), token, refreshTokenTimeout, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(tokenUtil.getRedisKey(username, "refreshToken"), refreshToken, refreshTokenTimeout, TimeUnit.DAYS);

        Util.responseJson(response, Result.ok(data));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        Util.responseJson(response, Result.create(ResponseCode.LOGIN_FAILURE));
    }
}

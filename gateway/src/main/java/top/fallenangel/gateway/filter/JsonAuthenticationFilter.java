package top.fallenangel.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.dto.UserDTO;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenUtil tokenUtil) {
        this.userRepository = userRepository;
        setAuthenticationManager(authenticationManager);

        this.tokenUtil = tokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            JSONObject bodyJson = JSON.parseObject(readBodyJson(request));

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
        Util.responseJson(response, Result.ok(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        Util.responseJson(response, Result.create(ResponseCode.LOGIN_FAILURE));
    }

    private String readBodyJson(HttpServletRequest request) {
        StringBuilder json = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (reader != null) {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return json.toString();
    }
}

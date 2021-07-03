package top.fallenangel.gateway.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoLoginEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Util.responseJson(response, Result.create(ResponseCode.UNAUTHORIZED));
    }
}

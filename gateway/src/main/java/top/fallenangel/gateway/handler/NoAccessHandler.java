package top.fallenangel.gateway.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoAccessHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        Util.responseJson(response, Result.create(ResponseCode.FORBIDDEN));
    }
}

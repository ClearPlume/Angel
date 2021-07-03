package top.fallenangel.response;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK(200, "OK", "连接已建立"),
    UNAUTHORIZED(401, "Unauthorized", "需要在请求头中携带Token信息(Authorization)"),
    FORBIDDEN(403, "Forbidden", "权限不足"),
    LOGIN_FAILURE(460, "Login Failure", "登录失败，检查用户名或密码"),
    TOKEN_INVALID(461, "Token Invalid", "非法Token"),
    ERROR(500, "Internal Server Error", "服务器内部错误");

    private final int code;
    private final String codeDetail;
    private final String msg;

    ResponseCode(int code, String codeDetail, String msg) {
        this.code = code;
        this.codeDetail = codeDetail;
        this.msg = msg;
    }
}

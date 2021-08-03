package top.fallenangel.response;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK(200, "OK", null),
    UNAUTHORIZED(401, "Unauthorized", "需要在请求头中携带Token信息(Authorization)，请登录以获取有效 Token"),
    FORBIDDEN(403, "Forbidden", "权限不足"),
    LOGIN_FAILURE(460, "Login Failure", "登录失败，检查用户名或密码"),
    TOKEN_INVALID(461, "Token Invalid", "非法Token"),
    TOKEN_EXPIRED(462, "Token Expired", "Token 过期"),
    REFRESH_TOKEN_INVALID(464, "Refresh Token Invalid", "非法 Refresh Token"),
    REFRESH_TOKEN_EXPIRED(463, "Refresh Token Expired", "Refresh Token 过期"),
    REGISTER_FAILURE(465, "Register Failure", "注册失败，用户名已存在"),
    USER_NOT_EXISTS(466, "User Not Exists", "不存在这个用户"),
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

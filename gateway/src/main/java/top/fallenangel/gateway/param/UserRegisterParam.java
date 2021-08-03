package top.fallenangel.gateway.param;

import lombok.Data;

@Data
public class UserRegisterParam {
    private String username;
    private String password;
    private String nickname;
    private String avatar;
}

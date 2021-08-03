package top.fallenangel.gateway.param;

import lombok.Data;

@Data
public class UserUpdateParam {
    private Integer id;
    private String username;
    private String nickname;
    private String avatar;
}

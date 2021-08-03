package top.fallenangel.gateway.vo;

import lombok.Data;

@Data
public class UserVO {
    private Integer id;
    private Integer roleId;
    private String username;
    private String nickname;
    private String avatar;
}

package top.fallenangel.gateway.dto.auth;

import lombok.Data;

import java.util.List;

@Data
public class SetRoleAuthParam {
    private Integer roleId;
    private List<Integer> authIds;
}

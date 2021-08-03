package top.fallenangel.gateway.param;

import lombok.Data;

import java.util.List;

@Data
public class SetRoleAuthParam {
    private Integer roleId;
    private List<Integer> authIds;
}

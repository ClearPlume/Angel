package top.fallenangel.gateway.trasfer;

import com.alibaba.fastjson.JSON;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.vo.RoleVO;

import java.util.List;
import java.util.stream.Collectors;

public class RoleTransfer {
    public static RoleVO entity2vo(RoleEntity entity) {
        return JSON.parseObject(JSON.toJSONString(entity), RoleVO.class);
    }

    public static List<RoleVO> entity2vo(List<RoleEntity> entities) {
        return entities.stream().map(RoleTransfer::entity2vo).collect(Collectors.toList());
    }
}

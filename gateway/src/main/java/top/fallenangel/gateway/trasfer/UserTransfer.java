package top.fallenangel.gateway.trasfer;

import com.alibaba.fastjson.JSON;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.vo.UserVO;

import java.util.List;
import java.util.stream.Collectors;

public class UserTransfer {
    public static UserVO entity2vo(UserEntity entity) {
        UserVO vo = JSON.parseObject(JSON.toJSONString(entity), UserVO.class);
        vo.setRoleId(entity.getRole().getId());
        return vo;
    }

    public static List<UserVO> entity2vo(List<UserEntity> entities) {
        return entities.stream().map(UserTransfer::entity2vo).collect(Collectors.toList());
    }
}

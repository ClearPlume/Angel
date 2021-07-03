package top.fallenangel.gateway.service;

import top.fallenangel.gateway.dto.auth.SetRoleAuthParam;
import top.fallenangel.gateway.entity.RoleEntity;

import java.util.List;

public interface IRoleService {
    List<RoleEntity> findAll();

    boolean setRoleAuth(SetRoleAuthParam param);
}

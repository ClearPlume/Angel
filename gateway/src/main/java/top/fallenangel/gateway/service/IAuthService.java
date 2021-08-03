package top.fallenangel.gateway.service;

import top.fallenangel.gateway.entity.ApiEntity;

import java.util.List;

public interface IAuthService {
    List<ApiEntity> selectAllByRoleId(Integer roleId);
}

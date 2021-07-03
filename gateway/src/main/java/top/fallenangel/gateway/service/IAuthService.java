package top.fallenangel.gateway.service;

import top.fallenangel.gateway.entity.AuthEntity;

import java.util.List;

public interface IAuthService {
    List<AuthEntity> selectAllByRoleId(Integer roleId);
}

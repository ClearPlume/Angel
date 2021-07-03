package top.fallenangel.gateway.service;

import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.entity.UserEntity;

public interface IUserService {
    RoleEntity findRoleByUsername(String username);

    UserEntity findByUsername(String username);
}

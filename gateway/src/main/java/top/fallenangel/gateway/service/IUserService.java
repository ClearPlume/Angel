package top.fallenangel.gateway.service;

import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.param.UserRegisterParam;
import top.fallenangel.gateway.param.UserUpdateParam;
import top.fallenangel.gateway.vo.UserVO;

import java.util.List;

public interface IUserService {
    UserVO save(UserRegisterParam user) throws RuntimeException;

    void delete(String username) throws RuntimeException;

    void update(UserUpdateParam user);

    RoleEntity findRoleByUsername(String username);

    UserVO findByUsername(String username);

    List<UserVO> findAll();

    UserVO updateUserRole(Integer userId, Integer roleId);
}

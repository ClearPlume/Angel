package top.fallenangel.gateway.service.impl;

import org.springframework.stereotype.Service;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RoleEntity findRoleByUsername(String username) {
        return userRepository.findByUsername(username).getRole();
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

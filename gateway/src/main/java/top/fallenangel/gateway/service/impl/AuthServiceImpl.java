package top.fallenangel.gateway.service.impl;

import org.springframework.stereotype.Service;
import top.fallenangel.gateway.entity.AuthEntity;
import top.fallenangel.gateway.repository.AuthRepository;
import top.fallenangel.gateway.service.IAuthService;

import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {
    private final AuthRepository authRepository;

    public AuthServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public List<AuthEntity> selectAllByRoleId(Integer roleId) {
        return authRepository.selectAllByRoleId(roleId);
    }
}

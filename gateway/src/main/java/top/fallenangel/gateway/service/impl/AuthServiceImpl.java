package top.fallenangel.gateway.service.impl;

import org.springframework.stereotype.Service;
import top.fallenangel.gateway.entity.ApiEntity;
import top.fallenangel.gateway.repository.ApiRepository;
import top.fallenangel.gateway.service.IAuthService;

import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {
    private final ApiRepository apiRepository;

    public AuthServiceImpl(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    public List<ApiEntity> selectAllByRoleId(Integer roleId) {
        return apiRepository.selectAllByRoleId(roleId);
    }
}

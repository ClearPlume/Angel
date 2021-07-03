package top.fallenangel.gateway.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.dto.auth.SetRoleAuthParam;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.repository.RoleRepository;
import top.fallenangel.gateway.service.IRoleService;
import top.fallenangel.gateway.util.Constraint;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final StringRedisTemplate redisTemplate;

    public RoleServiceImpl(RoleRepository roleRepository, StringRedisTemplate redisTemplate) {
        this.roleRepository = roleRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public boolean setRoleAuth(SetRoleAuthParam param) {
        Integer roleId = param.getRoleId();
        List<Integer> authIds = param.getAuthIds();

        roleRepository.deleteBatchById(roleId);
        for (Integer authId : authIds) {
            roleRepository.insert(roleId, authId);
        }

        redisTemplate.opsForValue().set(Constraint.AUTH_VERSION_KEY, Util.uuid());

        return true;
    }
}

package top.fallenangel.gateway.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.param.UserRegisterParam;
import top.fallenangel.gateway.param.UserUpdateParam;
import top.fallenangel.gateway.repository.RoleRepository;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.service.IUserService;
import top.fallenangel.gateway.trasfer.UserTransfer;
import top.fallenangel.gateway.vo.UserVO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserVO save(UserRegisterParam user) throws RuntimeException {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(roleRepository.selectByCode("user"));
        userEntity.setNickname(user.getNickname() == null ? user.getUsername() : user.getNickname());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setEnable(true);

        return UserTransfer.entity2vo(userRepository.save(userEntity));
    }

    @Override
    public void delete(String username) throws RuntimeException {
        userRepository.delete(userRepository.findByUsername(username));
    }

    @Override
    public void update(UserUpdateParam user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow();

        userEntity.setUsername(user.getUsername());
        userEntity.setNickname(user.getNickname());
        userEntity.setAvatar(user.getAvatar());

        userRepository.save(userEntity);
    }

    @Override
    public RoleEntity findRoleByUsername(String username) {
        return userRepository.findByUsername(username).getRole();
    }

    @Override
    public UserVO findByUsername(String username) {
        return UserTransfer.entity2vo(userRepository.findByUsername(username));
    }

    @Override
    public List<UserVO> findAll() {
        return UserTransfer.entity2vo(userRepository.findAll());
    }

    @Override
    public UserVO updateUserRole(Integer userId, Integer roleId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);

        user.setRole(role);

        return UserTransfer.entity2vo(userRepository.save(user));
    }
}

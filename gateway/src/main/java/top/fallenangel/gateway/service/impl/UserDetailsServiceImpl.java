package top.fallenangel.gateway.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.fallenangel.gateway.dto.UserDTO;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.length() == 0) {
            throw new RuntimeException("用户名<" + username + ">为空");
        }

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("不存在这样的用户：" + username);
        }

        System.out.println("登录中...");
        return new UserDTO(user);
    }
}

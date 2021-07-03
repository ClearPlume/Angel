package top.fallenangel.gateway.controller;

import org.springframework.web.bind.annotation.*;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.repository.RoleRepository;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("test")
public class TestController {
    private final IUserService userService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public TestController(IUserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("test")
    public Map<String, String> test() {
        Map<String, String> data = new HashMap<>();

        data.put("data1", "123");
        data.put("data2", "中文");
        data.put("msg", "test");

        return data;
    }

    @PostMapping("userList")
    public List<UserEntity> userList() {
        return userRepository.findAll();
    }

    @PostMapping("userRole/{username}")
    public RoleEntity userRole(@PathVariable String username) {
        return userService.findRoleByUsername(username);
    }

    @PostMapping("roleList")
    public List<RoleEntity> roleList() {
        return roleRepository.findAll();
    }
}

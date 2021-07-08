package top.fallenangel.gateway.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.gateway.service.IUserService;
import top.fallenangel.response.Result;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("userList")
    public Result<List<UserEntity>> userList() {
        return Result.ok(userService.findAll());
    }

    @PostMapping("userInfo/{username}")
    public Result<UserEntity> userInfo(@PathVariable String username) {
        return Result.ok(userService.findByUsername(username));
    }

    @PostMapping("userRole/{username}")
    public Result<RoleEntity> userRole(@PathVariable String username) {
        return Result.ok(userService.findRoleByUsername(username));
    }
}

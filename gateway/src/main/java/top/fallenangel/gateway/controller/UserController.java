package top.fallenangel.gateway.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fallenangel.gateway.entity.UserEntity;
import top.fallenangel.response.Result;
import top.fallenangel.gateway.service.IUserService;

@RestController
@RequestMapping("user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("userInfo/{username}")
    public Result<UserEntity> userRole(@PathVariable String username) {
        return Result.ok(userService.findByUsername(username));
    }
}

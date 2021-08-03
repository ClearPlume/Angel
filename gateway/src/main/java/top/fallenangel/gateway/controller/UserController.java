package top.fallenangel.gateway.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.param.UserRegisterParam;
import top.fallenangel.gateway.param.UserUpdateParam;
import top.fallenangel.gateway.service.IUserService;
import top.fallenangel.gateway.vo.UserVO;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public Result<UserVO> register(@RequestBody UserRegisterParam user) {
        try {
            return Result.ok(userService.save(user));
        } catch (RuntimeException e) {
            return Result.create(ResponseCode.REGISTER_FAILURE);
        }
    }

    @DeleteMapping("delete/{username}")
    public Result<Void> delete(@PathVariable String username) {
        try {
            userService.delete(username);
            return Result.ok();
        } catch (RuntimeException e) {
            return Result.create(ResponseCode.USER_NOT_EXISTS);
        }
    }

    @PutMapping("modify")
    public Result<Void> modify(@RequestBody UserUpdateParam user) {
        userService.update(user);
        return Result.ok();
    }

    @GetMapping("list")
    public Result<List<UserVO>> list() {
        return Result.ok(userService.findAll());
    }

    @GetMapping("info")
    public Result<UserVO> info(Authentication authentication) {
        return Result.ok(userService.findByUsername(authentication.getName()));
    }

    @GetMapping("role/{username}")
    public Result<RoleEntity> role(@PathVariable String username) {
        return Result.ok(userService.findRoleByUsername(username));
    }
}

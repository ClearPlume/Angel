package top.fallenangel.gateway.controller;

import org.springframework.web.bind.annotation.*;
import top.fallenangel.gateway.entity.ApiEntity;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.gateway.param.SetRoleAuthParam;
import top.fallenangel.gateway.service.IAuthService;
import top.fallenangel.gateway.service.IRoleService;
import top.fallenangel.gateway.service.IUserService;
import top.fallenangel.gateway.vo.UserVO;
import top.fallenangel.response.Result;

import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final IRoleService roleService;
    private final IAuthService authService;
    private final IUserService userService;

    public AuthController(IRoleService roleService, IAuthService authService, IUserService userService) {
        this.roleService = roleService;
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("roleList")
    public Result<List<RoleEntity>> getRoleList() {
        return Result.ok(roleService.findAll());
    }

    @GetMapping("getRoleAuth/{roleId}")
    public Result<List<ApiEntity>> getRoleAuth(@PathVariable Integer roleId) {
        return Result.ok(authService.selectAllByRoleId(roleId));
    }

    @PutMapping("setRoleAuth")
    public Result<Void> setRoleAuth(@RequestBody SetRoleAuthParam param) {
        boolean success = roleService.setRoleAuth(param);

        if (success) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @PutMapping("assignUserRole/{userId}/{roleId}")
    public Result<UserVO> assignUserRole(@PathVariable Integer userId, @PathVariable Integer roleId) {
        return Result.ok(userService.updateUserRole(userId, roleId));
    }
}

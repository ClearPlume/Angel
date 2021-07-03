package top.fallenangel.gateway.controller;

import org.springframework.web.bind.annotation.*;
import top.fallenangel.gateway.dto.auth.SetRoleAuthParam;
import top.fallenangel.gateway.entity.AuthEntity;
import top.fallenangel.gateway.entity.RoleEntity;
import top.fallenangel.response.Result;
import top.fallenangel.gateway.service.IAuthService;
import top.fallenangel.gateway.service.IRoleService;

import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final IRoleService roleService;
    private final IAuthService authService;

    public AuthController(IRoleService roleService, IAuthService authService) {
        this.roleService = roleService;
        this.authService = authService;
    }

    @PostMapping("roleList")
    public Result<List<RoleEntity>> getRoleList() {
        return Result.ok(roleService.findAll());
    }

    @PostMapping("getRoleAuth/{roleId}")
    public Result<List<AuthEntity>> getRoleAuth(@PathVariable Integer roleId) {
        return Result.ok(authService.selectAllByRoleId(roleId));
    }

    @PostMapping("setRoleAuth")
    public Result<Void> setRoleAuth(@RequestBody SetRoleAuthParam param) {
        boolean success = roleService.setRoleAuth(param);

        if (success) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }
}

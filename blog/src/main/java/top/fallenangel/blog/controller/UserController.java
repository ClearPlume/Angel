package top.fallenangel.blog.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fallenangel.blog.feign.GatewayFeign;
import top.fallenangel.response.Result;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    private final GatewayFeign gatewayFeign;

    public UserController(GatewayFeign gatewayFeign) {
        this.gatewayFeign = gatewayFeign;
    }

    @PostMapping("userInfo/{username}")
    public Result<Map<String, Object>> userInfo(@PathVariable String username) {
        return gatewayFeign.userInfo(username);
    }
}

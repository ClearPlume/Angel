package top.fallenangel.blog.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import top.fallenangel.response.Result;

import java.util.Map;

@FeignClient("gateway")
public interface GatewayFeign {
    @PostMapping("user/userInfo/{username}")
    Result<Map<String, Object>> userInfo(@PathVariable("username") String username);
}

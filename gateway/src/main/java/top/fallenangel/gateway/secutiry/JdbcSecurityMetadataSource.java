package top.fallenangel.gateway.secutiry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.entity.ApiEntity;
import top.fallenangel.gateway.repository.ApiRepository;
import top.fallenangel.gateway.repository.RoleRepository;
import top.fallenangel.gateway.util.Constraint;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JdbcSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final StringRedisTemplate redisTemplate;
    private final ApiRepository apiRepository;
    private final RoleRepository roleRepository;

    private String authVersion;
    private Map<RequestMatcher, List<ConfigAttribute>> metadata;

    public JdbcSecurityMetadataSource(StringRedisTemplate redisTemplate, ApiRepository apiRepository, RoleRepository roleRepository) {
        this.redisTemplate = redisTemplate;
        this.apiRepository = apiRepository;
        this.roleRepository = roleRepository;

        authVersion = getAuthVersion();
        metadata = getMetadata();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            log.info("获取资源<{}>的权限信息", invocation);
            String uri = invocation.getHttpRequest().getRequestURI();
            String method = invocation.getHttpRequest().getMethod();
            RequestMatcher matcher = new AntPathRequestMatcher(uri, method);

            if (authVersion == null || !authVersion.equals(getAuthVersion())) {
                metadata = getMetadata();
                setAuthVersion();
            }

            List<ConfigAttribute> auths = metadata.get(matcher);
            log.info("资源<{}>的权限信息：{}", invocation, auths);
            return auths;
        }

        throw new IllegalArgumentException("Object must be a non-null FilterInvocation");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private String getAuthVersion() {
        String authVersion = redisTemplate.opsForValue().get(Constraint.AUTH_VERSION_KEY);
        log.info("获取当前权限版本：<{}>", authVersion);
        return authVersion;
    }

    private void setAuthVersion() {
        String version = Util.uuid();

        redisTemplate.opsForValue().set(Constraint.AUTH_VERSION_KEY, version);
        authVersion = version;
        log.info("设置新的权限版本：<{}>", version);
    }

    private Map<RequestMatcher, List<ConfigAttribute>> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<>();
        } else {
            metadata.clear();
        }
        List<ApiEntity> authEntities = apiRepository.findAll();

        for (ApiEntity apiEntity : authEntities) {
            if (!apiEntity.getUri().isBlank()) {
                List<String> roles = roleRepository.selectAllCodeByAuthUri(apiEntity.getUri());
                metadata.put(new AntPathRequestMatcher(apiEntity.getUri(), apiEntity.getMethod()), roles.stream()
                        .map(role -> new SecurityConfig("ROLE_" + role))
                        .collect(Collectors.toList()));
            }
        }

        log.info("所有资源的权限信息：");
        metadata.forEach((uri, auth) -> log.info("{} === {}", uri, auth));
        return metadata;
    }
}

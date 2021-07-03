package top.fallenangel.gateway.secutiry;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.entity.AuthEntity;
import top.fallenangel.gateway.repository.AuthRepository;
import top.fallenangel.gateway.repository.RoleRepository;
import top.fallenangel.gateway.util.Constraint;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JdbcSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final StringRedisTemplate redisTemplate;
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;

    private String authVersion;
    private Map<RequestMatcher, List<ConfigAttribute>> metadata;

    public JdbcSecurityMetadataSource(StringRedisTemplate redisTemplate, AuthRepository authRepository, RoleRepository roleRepository) {
        this.redisTemplate = redisTemplate;
        this.authRepository = authRepository;
        this.roleRepository = roleRepository;

        authVersion = getAuthVersion();
        metadata = getMetadata();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (object instanceof FilterInvocation) {
            FilterInvocation invocation = (FilterInvocation) object;
            String uri = invocation.getHttpRequest().getRequestURI();
            RequestMatcher matcher = new AntPathRequestMatcher(uri);

            if (authVersion == null || !authVersion.equals(getAuthVersion())) {
                metadata = getMetadata();
                setAuthVersion();
            }

            return metadata.get(matcher);
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
        return redisTemplate.opsForValue().get(Constraint.AUTH_VERSION_KEY);
    }

    private void setAuthVersion() {
        String version = Util.uuid();

        redisTemplate.opsForValue().set(Constraint.AUTH_VERSION_KEY, version);
        authVersion = version;
    }

    private Map<RequestMatcher, List<ConfigAttribute>> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<>();
        } else {
            metadata.clear();
        }
        List<AuthEntity> authEntities = authRepository.findAll();

        for (AuthEntity authEntity : authEntities) {
            if (!authEntity.getUri().isBlank()) {
                List<String> roles = roleRepository.selectAllCodeByAuthUri(authEntity.getUri());
                metadata.put(new AntPathRequestMatcher(authEntity.getUri()), roles.stream().map(SecurityConfig::new).collect(Collectors.toList()));
            }
        }

        return metadata;
    }
}

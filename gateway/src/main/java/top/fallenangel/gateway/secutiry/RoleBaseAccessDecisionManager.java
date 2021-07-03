package top.fallenangel.gateway.secutiry;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import top.fallenangel.gateway.dto.UserDTO;

import java.util.Collection;

@Component
public class RoleBaseAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (authentication.getDetails() instanceof UserDTO) {
            UserDTO user = (UserDTO) authentication.getDetails();
            String authority = user.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
            SecurityConfig config = new SecurityConfig(authority);

            if (configAttributes.contains(config)) {
                return;
            }
        }

        throw new AccessDeniedException("访问权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}

package top.fallenangel.gateway.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import top.fallenangel.gateway.secutiry.UserDTO;
import top.fallenangel.gateway.repository.UserRepository;

@Component
public class AuthenticationUtil {
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public AuthenticationUtil(TokenUtil tokenUtil, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
    }

    public Authentication authentication(String token) throws Exception {
        String username = tokenUtil.getUsername(token);
        UserDTO userDTO = new UserDTO(userRepository.findByUsername(username));

        if (username == null) {
            return null;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDTO.getAuthorities());
        authentication.setDetails(userDTO);

        return authentication;
    }
}

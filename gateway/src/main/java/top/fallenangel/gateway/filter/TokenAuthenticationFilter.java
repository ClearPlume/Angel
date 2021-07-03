package top.fallenangel.gateway.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.fallenangel.common.Util;
import top.fallenangel.gateway.dto.UserDTO;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.util.TokenUtil;
import top.fallenangel.response.ResponseCode;
import top.fallenangel.response.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, TokenUtil tokenUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(TokenUtil.TOKEN_HEADER);

        if (token == null) {
            Util.responseJson(response, Result.create(ResponseCode.UNAUTHORIZED));
            return;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null) {
            try {
                securityContext.setAuthentication(authentication(token));
            } catch (JWTVerificationException e) {
                Util.responseJson(response, Result.create(ResponseCode.TOKEN_INVALID));
                return;
            }
        }

        super.doFilterInternal(request, response, chain);
    }

    private Authentication authentication(String token) throws JWTVerificationException {
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

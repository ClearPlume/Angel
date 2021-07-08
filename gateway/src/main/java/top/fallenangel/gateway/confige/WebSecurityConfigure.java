package top.fallenangel.gateway.confige;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.fallenangel.gateway.filter.JsonAuthenticationFilter;
import top.fallenangel.gateway.filter.LogoutProcessingFilter;
import top.fallenangel.gateway.filter.TokenAuthenticationFilter;
import top.fallenangel.gateway.handler.NoAccessHandler;
import top.fallenangel.gateway.handler.NoLoginEntryPoint;
import top.fallenangel.gateway.repository.UserRepository;
import top.fallenangel.gateway.secutiry.JdbcSecurityMetadataSource;
import top.fallenangel.gateway.secutiry.RoleBaseAccessDecisionManager;
import top.fallenangel.gateway.service.impl.UserDetailsServiceImpl;
import top.fallenangel.gateway.util.AuthenticationUtil;
import top.fallenangel.gateway.util.TokenUtil;

@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;
    private final TokenUtil tokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final UserRepository userRepository;

    private final LogoutProcessingFilter logoutProcessingFilter;
    private final JdbcSecurityMetadataSource securityMetadataSource;
    private final RoleBaseAccessDecisionManager accessDecisionManager;
    private final NoAccessHandler noAccessHandler;
    private final NoLoginEntryPoint noLoginEntryPoint;

    private final StringRedisTemplate redisTemplate;

    public WebSecurityConfigure(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, TokenConfig tokenConfig, TokenUtil tokenUtil, AuthenticationUtil authenticationUtil, UserRepository userRepository, LogoutProcessingFilter logoutProcessingFilter, JdbcSecurityMetadataSource securityMetadataSource, RoleBaseAccessDecisionManager accessDecisionManager, NoAccessHandler noAccessHandler, NoLoginEntryPoint noLoginEntryPoint, StringRedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
        this.tokenUtil = tokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.userRepository = userRepository;
        this.logoutProcessingFilter = logoutProcessingFilter;
        this.securityMetadataSource = securityMetadataSource;
        this.accessDecisionManager = accessDecisionManager;
        this.noAccessHandler = noAccessHandler;
        this.noLoginEntryPoint = noLoginEntryPoint;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O securityInterceptor) {

                        securityInterceptor.setSecurityMetadataSource(securityMetadataSource);
                        securityInterceptor.setAccessDecisionManager(accessDecisionManager);

                        return securityInterceptor;
                    }
                });

        http.exceptionHandling()
                .accessDeniedHandler(noAccessHandler)
                .authenticationEntryPoint(noLoginEntryPoint);

        http
                .addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(tokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(logoutProcessingFilter, LogoutFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();
        http.cors();
    }

    private JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        return new JsonAuthenticationFilter(authenticationManager(), userRepository, tokenUtil, tokenConfig, redisTemplate);
    }

    private TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        return new TokenAuthenticationFilter(authenticationManager(), tokenUtil, authenticationUtil, redisTemplate);
    }
}

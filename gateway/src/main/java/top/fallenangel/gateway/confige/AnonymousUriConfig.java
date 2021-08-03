package top.fallenangel.gateway.confige;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties("anonymous")
public class AnonymousUriConfig {
    private List<String> uri;

    public boolean isAnonymous(HttpServletRequest request) {
        List<RequestMatcher> requestMatchers = uri.stream().map(uri -> {
            String[] u = uri.split(":");
            return new AntPathRequestMatcher(u[1], u[0]);
        }).collect(Collectors.toList());

        for (RequestMatcher requestMatcher : requestMatchers) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }

        return false;
    }

    public List<String> getUri() {
        return uri;
    }

    public void setUri(List<String> uri) {
        this.uri = uri;
    }
}

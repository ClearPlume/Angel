package top.fallenangel.gateway.confige;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token")
public class TokenConfig {
    /**
     * token 在Redis中的key
     */
    private String key;

    /**
     * 登录令牌的过期时间，单位是分
     */
    private int timeout;

    /**
     * 刷新令牌的过期时间，单位是天
     */
    private int refreshTimeout;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRefreshTimeout() {
        return refreshTimeout;
    }

    public void setRefreshTimeout(int refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
    }
}

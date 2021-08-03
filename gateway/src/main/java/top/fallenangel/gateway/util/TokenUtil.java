package top.fallenangel.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import top.fallenangel.gateway.confige.TokenConfig;
import top.fallenangel.gateway.secutiry.UserDTO;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {
    public static final String TOKEN_HEADER = "Authorization";

    private static final String ISSUER = "the_FallenAngel";
    private static final String SECRET = "asd456&*(";

    private final TokenConfig tokenConfig;

    public TokenUtil(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public String createToken(UserDetails userDetails) {
        UserDTO user = (UserDTO) userDetails;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, tokenConfig.getTimeout());

        Map<String, Object> claims = new HashMap<>();

        claims.put("expires", calendar.getTime());

        return generateToken(claims, user.getUsername());
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return JWT.create()
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .withPayload(claims)
                .withSubject(subject)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String getUsername(String token) throws Exception {
        return decode(token).getSubject();
    }

    public boolean isExpiration(String token) throws Exception {
        return !decode(token).getClaim("expires").asDate().after(new Date());
    }

    public String getRedisKey(String username, String keyType) {
        return String.format("%s:%s:%s", tokenConfig.getKey(), username, keyType);
    }

    @SuppressWarnings("RedundantThrows")
    private DecodedJWT decode(String token) throws Exception {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}

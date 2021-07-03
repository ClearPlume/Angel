package top.fallenangel.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import top.fallenangel.gateway.dto.UserDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Component
public class TokenUtil {
    public static final String TOKEN_HEADER = "Authorization";

    private static final String ISSUER = "the_FallenAngel";
    private static final String SECRET = "asd456&*(";

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${user.token.key}")
    private String userTokenKey;

    public TokenUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String createToken(UserDetails userDetails) {
        UserDTO user = (UserDTO) userDetails;
        return generateToken(null, user.getUsername());
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        String token = JWT.create()
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .withPayload(claims)
                .withSubject(subject)
                .sign(Algorithm.HMAC256(SECRET));

        stringRedisTemplate.opsForValue().set(userTokenKey + ":" + subject, token);

        return token;
    }

    public String getUsername(String token) throws JWTVerificationException {
        return decode(token).getSubject();
    }

    public boolean isExpiration(String token) throws JWTVerificationException {
        return decode(token).getExpiresAt().before(new Date());
    }

    public boolean validateToken(String token, String username) throws JWTVerificationException {
        return !isExpiration(token) && username.equals(getUsername(token));
    }

    public void refreshToken(String username) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        try {
            Date tomorrow = formatter.parse(formatter.format(calendar.getTime()));
            stringRedisTemplate.expireAt(userTokenKey + ":" + username, tomorrow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private DecodedJWT decode(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
}

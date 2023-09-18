package OOTWhongik.OOTW.global.config.security;

import OOTWhongik.OOTW.domain.member.domain.Member;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtils {

    private static String secretKey;

    private static String expirationTimeHour;

    @Value("${jwt.secret_key}")
    public void setSecretKey(String secretKey) {
        JwtUtils.secretKey = secretKey;
    }

    @Value("${jwt.expiration_time_hour}")
    public void setExpirationTimeHour(String expirationTimeHour) {
        JwtUtils.expirationTimeHour = expirationTimeHour;
    }

    public static String createToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(Long.parseLong(expirationTimeHour)).toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

//    public boolean validateToken(String token) {
//        if (!StringUtils.hasText(token)) {
//            throw new AppException(ErrorCode.JWT_TOKEN_NOT_EXISTS);
//        }
//        if(isLogout(token)){
//            throw new AppException(ErrorCode.JWT_TOKEN_EXPIRED);
//        }
//        try {
//            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
//            return true;
//        } catch (SignatureException | MalformedJwtException e) {
//            throw new AppException(ErrorCode.WRONG_JWT_TOKEN);
//        } catch (ExpiredJwtException e) {
//            throw new AppException(ErrorCode.JWT_TOKEN_EXPIRED);
//        }
//    }

    public static Long getExpiration(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - new Date().getTime();
    }

    public static String getEmailFromToken(String token) {
        return (String) getClaims(token).get("email");
    }

    public static String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    public static Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public static String resolveAccessToken(HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            return jwtHeader.replace("Bearer ", "");
        } else {
            return null;
        }
    }
}

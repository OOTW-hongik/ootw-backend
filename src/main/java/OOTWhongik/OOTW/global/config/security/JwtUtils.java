package OOTWhongik.OOTW.global.config.security;

import OOTWhongik.OOTW.domain.member.domain.Member;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class JwtUtils {

    private static final String AUTHORITIES_KEY = "role";

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
        claims.put("sub", member.getId());
        claims.put("role", member.getRole());
        claims.put("name", member.getName());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(Long.parseLong(expirationTimeHour)).toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException ex) {
            return false;
        }
    }

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

    public static Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private static Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

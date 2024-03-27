package OOTWhongik.OOTW.auth.util;

import OOTWhongik.OOTW.member.domain.Member;
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

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private static final String AUTHORITIES_KEY = "role";

    private static String secretKey;

    private static Long expirationTimeHour;

    //cannot field inject to static properties
    //@Value can use on setter
    @Value("${jwt.secret_key}")
    public void setSecretKey(String secretKey) {
        JwtUtil.secretKey = secretKey;
    }

    @Value("${jwt.expiration_time_hour}")
    public void setExpirationTimeHour(Long expirationTimeHour) {
        JwtUtil.expirationTimeHour = expirationTimeHour;
    }

    public static String createToken(Member member) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + Duration.ofHours(expirationTimeHour).toMillis());
        return Jwts.builder()
                .setIssuer("OOTW-hongik")
                .setIssuedAt(issuedAt)
                .setSubject(member.getId().toString())
                .claim("role", member.getRole())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static String resolveToken(HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            return jwtHeader.replace("Bearer ", "");
        } else {
            return null;
        }
    }

    public static Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private static Claims parseClaims(String accessToken) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(accessToken)
                .getBody();
    }
}

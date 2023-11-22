package OOTWhongik.OOTW.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        String accessToken = JwtUtils.resolveAccessToken(request);
        if (JwtUtils.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            setErrorResponse(request, response, new JwtException("올바른 토큰이 아닙니다."));
        }

    }

    private void setAuthentication(String token) {
        Authentication authentication = JwtUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException ex) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String, Object> body = new HashMap<>();
        body.put("result", "error");
        body.put("httpStatus", HttpStatus.UNAUTHORIZED.value());
        body.put("message", ex.getMessage());
        body.put("data", null);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/login") || path.startsWith("/health_check") || path.startsWith("/oauth");
    }
}
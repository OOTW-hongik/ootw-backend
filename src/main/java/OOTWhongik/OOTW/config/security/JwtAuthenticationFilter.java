package OOTWhongik.OOTW.config.security;

import OOTWhongik.OOTW.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Token에서 Claim 꺼내기
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){ //header에 AUTHORIZATION이 없거나, Bearer로 시작하지 않으면 filter
            log.error("header가 없거나, 형식이 틀립니다. - {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }

        String token;
        try {
            token = authorizationHeader.split(" ")[1].trim();
        } catch (Exception e) {
            log.error("토큰을 분리하는데 실패했습니다. - {}", authorizationHeader);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("token : {}", token);

        //토큰이 Valid한지 확인
        if(JwtUtil.getExpiration(token) < 0){
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = JwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // SecurityUtil.getMemberId() 로 현재 로그인된 ID 찾아올 수 있음
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/login") || path.startsWith("/health_check") || path.startsWith("/oauth");
    }
}

package spring.boot.webcococo.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.boot.webcococo.utils.JwtTokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
//@AllArgsConstructor
@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        final String authHeader = request.getHeader("Authorization");


        try {
            // Nếu là các request không cần xác thực, bỏ qua filter
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                if (isTokenNotRequired(request) || isBypassToken(request)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // Trích xuất token và xác thực người dùng
            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractByEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Lỗi trong filter chain: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Request {} processed in {} ms", request.getRequestURI(), duration);
        }


    }

    private boolean isTokenNotRequired(@NonNull HttpServletRequest request) {
        List<Pair<String, String>> tokenNotRequiredEndpoints = Arrays.asList(
                Pair.of(apiPrefix + "/slider/getAllSlider", "GET"),
                Pair.of(apiPrefix + "/software/getAllSoftWare", "GET")
        );

        return tokenNotRequiredEndpoints.stream()
                .anyMatch(endpoint -> request.getServletPath().startsWith(endpoint.getFirst()) &&
                        request.getMethod().equalsIgnoreCase(endpoint.getSecond()));
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        List<Pair<String, String>> bypassEndpoints = Arrays.asList(
                Pair.of("/ws/info", "GET"),
                Pair.of("/ws", "GET"),
                Pair.of("/users/login", "POST"),
                Pair.of("/posts", "GET"),
                Pair.of("/payment/vn-pay-callback", "GET"),


                Pair.of("/users/register", "POST")
        );

        return bypassEndpoints.stream()
                .anyMatch(endpoint -> request.getServletPath().contains(endpoint.getFirst()) &&
                        request.getMethod().equalsIgnoreCase(endpoint.getSecond()));
    }
}

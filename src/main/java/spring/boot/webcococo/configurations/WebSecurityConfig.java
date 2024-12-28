package spring.boot.webcococo.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import spring.boot.webcococo.filters.JwtTokenFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private  JwtTokenFilter jwtTokenFilter;

//    @Value("${api.prefix}")
//    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                                .sessionCreationPolicy(STATELESS)
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set stateless session management
                                .maximumSessions(1)
                                .expiredUrl("/session-expired")
//                        .sessionFixation().newSession()
                )



                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers("/api/v1/users/register").permitAll()
                            .requestMatchers("/api/v1/payment/vn-pay-callback").permitAll()
                            .requestMatchers("/ws/info").permitAll()
                            .requestMatchers("/ws").permitAll()
                            .requestMatchers("/api/v1/posts").permitAll()
                            .requestMatchers("/api/v1/users/login").permitAll()
                            .anyRequest().authenticated();
                })
//                .authenticationProvider(authenticationProvider)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(STATELESS))  // Set stateless session management
                .csrf(csrf -> csrf.disable());





//        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
//                CorsConfiguration configuration = new CorsConfiguration();
//                configuration.setAllowedOrigins(List.of("*"));
//                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//                configuration.setExposedHeaders(List.of("x-auth-token"));
//                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                source.registerCorsConfiguration("/**", configuration);
//                httpSecurityCorsConfigurer.configurationSource(source);
//            }
//        });

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}

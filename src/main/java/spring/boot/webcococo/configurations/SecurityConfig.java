package spring.boot.webcococo.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CustomUserDetails;
import spring.boot.webcococo.repositories.UserRepository;
import spring.boot.webcococo.services.impl.UserPermissionServiceImpl;

import java.util.List;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final UserPermissionServiceImpl userPermissionService;

    @Bean
    public UserDetailsService userDetailsService() {
        log.error("Đã chạy vào đây");

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                log.error("Đã vào loadUserByUsername");

                // Tìm người dùng qua email
                var user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND,"khong tim thay email nay"));

                // Kiểm tra nếu username hoặc password là null
                if (user.getEmail() == null || user.getPassword() == null) {
                    log.error("User {} has null username or password", user.getEmail(),user.getPassword());
                    throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED, "Tài khoản này không hợp lệ");
                }

                // Lấy quyền cho người dùng
                List<GrantedAuthority> authorities = userPermissionService.getAuthoritiesForUserById(user.getId());

                // Kiểm tra nếu authorities bị null hoặc rỗng
                if (authorities == null || authorities.isEmpty()) {
                    log.error("User {} has no roles assigned", user.getEmail());
                    throw new AppException(ErrorCodeEnum.USER_NOT_EXISTED, "Tài khoản này không có role");
                }
                log.error("Đã đến bước cuối - Email: {}, Password: {}, Authorities: {}", user.getEmail(), user.getPassword(), authorities);

                return new CustomUserDetails(user, authorities);
//                return new User(
//                        user.getEmail(),
//                        user.getPassword(),
//                        authorities
//                );
            }
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
//        log.error("da chay vao day2");

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
//        log.error("da chay vao day 3");

        return config.getAuthenticationManager();
    }
}

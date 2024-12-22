package spring.boot.webcococo.utils;//package spring.boot.authenauthor.utils;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.entities.Users;
//import spring.boot.authenauthor.repositories.UserRepository;
//import spring.boot.authenauthor.services.impl.UserPermissionServiceImpl;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Component
//public class AuthenticationUtils {
//    private final UserPermissionServiceImpl userPermissionService;
//    private final UserRepository userRepository;
//
//    public Mono<Authentication> createAuthentication(String username, String password) {
//        // Tạo danh sách authorities (quyền) từ roles của UserDTO
//        return userRepository.findByUsername(username)
//                .switchIfEmpty(Mono.error(new RuntimeException("Không tồn tại user này"))) // Nếu không tìm thấy người dùng
//                .flatMap(users -> {
//                    var authorities = userPermissionService.getAuthoritiesForUser(users.getId());
//
//                    // Tạo đối tượng UserDetails từ thông tin của UserDTO
//                    UserDetails userDetails = User.builder()
//                            .username(username)
//                            .password(password) // Mật khẩu này nên được mã hóa
//                            .authorities(authorities)
//                            .build();
//
//                    // Tạo đối tượng Authentication từ UserDetails
//                    return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authorities));
//                });
//    }
//}

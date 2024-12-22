package spring.boot.webcococo.services.impl;//package spring.boot.authenauthor.services.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Service;
//import spring.boot.authenauthor.repositories.UserRepository;
//
//import java.util.List;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserPermissionServiceImpl userPermissionService;
//
//    @Autowired
//    private UserRepository usersRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return usersRepository.findByEmail(email)
//                .map(user -> {
//                    // Lấy danh sách quyền của người dùng
//                    List<GrantedAuthority> authorities = userPermissionService.getAuthoritiesForUserById(user.getId());
//                    // Tạo đối tượng UserDetails với thông tin người dùng và quyền
//                    return new User(user.getUsername(), user.getPassword(), authorities);
//                })
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//}

package spring.boot.webcococo.components;//package spring.boot.authenauthor.components;
//
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import spring.boot.authenauthor.entities.Permission;
//import spring.boot.authenauthor.entities.Users;
//import spring.boot.authenauthor.repositories.PermissionRepository;
//import spring.boot.authenauthor.repositories.UsersPermissionRepository;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//
//@AllArgsConstructor
//@Component
//@RequiredArgsConstructor
//public class CustomUserDetails implements UserDetails {
//
//    @Autowired
//    private UsersPermissionRepository usersPermissionRepository;
//    @Autowired
//    private PermissionRepository permissionRepository;
//
//    private Users user;
//
//
//
//    // Load authorities from repositories
//    private Collection<? extends GrantedAuthority> loadAuthorities(Users user) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        List<Integer> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(user.getId());
//        List<Permission> permissionList = permissionRepository.findByIdIn(permissionIdList);
//        for (Permission permission : permissionList) {
//            authorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission.getName().toUpperCase()));
//        }
//        return authorities;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return loadAuthorities(user);
//    }
//
//
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}

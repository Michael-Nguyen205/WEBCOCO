package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity

public class Users   extends BaseEntity  {


    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String name;

    private String username;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;


    private String email;

    private String password;


    @Column(name = "is_active")
    private Boolean isActive;


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Users() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Phương thức này sẽ được cập nhật trong UserDetailsService
//        return Collections.emptyList();
//    }





//
//    @Override
//    public String getUsername() {
//        return username; // Changed from email to username
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
}



//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<Posts> posts;
//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<Products> products;
//
//    @OneToMany
//    @JoinColumn(name = "users_id")
//    private Set<UserPermission> userPermissions;
//    // Other fields and methods...

//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        List<Integer> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(id);
//        List<Permission> permissionList = permissionRepository.findByIdIn(permissionIdList);
//
//        // Lấy tất cả các quyền của người dùng từ UserPermissions
//        for (Permission userPermission : permissionList) {
//            authorities.add(new SimpleGrantedAuthority("PERMISSION_" + userPermission.getName().toUpperCase()));
//        }
//
//  
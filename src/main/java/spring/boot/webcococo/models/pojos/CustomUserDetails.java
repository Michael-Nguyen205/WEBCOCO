package spring.boot.webcococo.models.pojos;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.boot.webcococo.entities.Users;

import java.util.Collection;



@Data
@ToString
public class CustomUserDetails implements UserDetails {
    private final Users user;
    private final Collection<? extends GrantedAuthority> authorities;



    @Override
    public String getUsername() {
        return user.getEmail(); // Hoặc bất kỳ thuộc tính nào bạn muốn sử dụng
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hoặc logic của bạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Hoặc logic của bạn
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Hoặc logic của bạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Hoặc logic của bạn
    }
}

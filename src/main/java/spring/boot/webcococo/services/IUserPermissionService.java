package spring.boot.webcococo.services;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface IUserPermissionService {

    List<GrantedAuthority> getAuthoritiesForUserById(String userId);
}

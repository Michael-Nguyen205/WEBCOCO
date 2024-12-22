package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Permission;
import spring.boot.webcococo.repositories.PermissionRepository;
import spring.boot.webcococo.repositories.UsersPermissionRepository;
import spring.boot.webcococo.services.IUserPermissionService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserPermissionServiceImpl implements IUserPermissionService {

    @Autowired
    private UsersPermissionRepository usersPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<GrantedAuthority> getAuthoritiesForUserById(String userId) {

        log.error("userId:{}",userId);
        // Lấy danh sách permissionId từ UsersPermissionRepository
        List<Long> permissionIdList = usersPermissionRepository.findAllPermissionByUserId(userId);

        // Lấy danh sách Permission từ PermissionRepository dựa trên permissionIdList
        List<Permission> permissions = permissionRepository.findAllById(permissionIdList.stream()
                .map(Long::intValue)  // Chuyển đổi Long thành Integer nếu cần
                .collect(Collectors.toList()));

        // Chuyển đổi danh sách Permission thành danh sách GrantedAuthority
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission.getName().toUpperCase()))
                .collect(Collectors.toList());
    }
}



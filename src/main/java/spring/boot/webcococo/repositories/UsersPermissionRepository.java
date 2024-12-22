package spring.boot.webcococo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.UserPermission;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersPermissionRepository extends JpaRepository<UserPermission, Integer> {

    Optional<UserPermission> findByUserIdAndPermissionId(String userId, Integer permissionId);

    Long deleteUserPermissionByUserIdAndPermissionId(String userId, Integer permissionId);

    @Query(value = "SELECT permission_id FROM user_permissions WHERE user_id = :userId", nativeQuery = true)
    List<Long> findAllPermissionByUserId(String userId);
}

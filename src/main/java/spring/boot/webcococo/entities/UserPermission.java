package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@Table(name = "user_permissions")
public class UserPermission extends BaseEntity {

@Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "permission_id")
    private Integer permissionId;

    // Getters and Setters

    public UserPermission() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }

}

package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity

@Table(name = "permission_model")
public class PermissionModel extends BaseEntity {

    @Id
    private String id;

    @NotBlank
    @Column(name = "model_id")
    private Integer modelId;

    @NotBlank
    @Column(name = "permission_id")
    private Integer permissionId;

    public PermissionModel() {
        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
    }

}

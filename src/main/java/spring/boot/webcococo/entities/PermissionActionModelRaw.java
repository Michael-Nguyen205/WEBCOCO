package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission_model_action_raw")
@Entity
public class PermissionActionModelRaw extends BaseEntity {

    @Id
    private String id;

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "action_id")
    private Integer actionId;

    @Column(name = "raw_id")
    private Integer rawId;

    // Nếu bạn muốn tạo UUID mới khi khởi tạo

}

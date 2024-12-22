package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="packages_feature")
public class PackagesFeature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "feature")
    private String  feature;

    @Column(name = "package_id")
    private Integer packageId;
}

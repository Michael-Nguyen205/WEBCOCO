package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AddOnServiceDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "addon_service_id")
    private Long addonServiceId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;
}

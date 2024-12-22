package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductsFeature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "products_id")
    private Integer productsId;
}

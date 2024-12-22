package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")

public class Products extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "categories_id")
    private Integer categoriesId;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "main_images")
    private String mainImages;

    @Column(name = "base_price")
    private Integer basePrice;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;
}

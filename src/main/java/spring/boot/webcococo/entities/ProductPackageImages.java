package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_package_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductPackageImages {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 7;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "product_id")
    private Integer productId;



}

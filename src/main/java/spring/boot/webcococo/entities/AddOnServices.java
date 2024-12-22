package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AddOnServices extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "categories_id")
    private Long categoriesId;
}

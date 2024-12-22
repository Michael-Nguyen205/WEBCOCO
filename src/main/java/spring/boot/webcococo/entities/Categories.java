package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Categories extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "parent_id")
    private Integer parentId;
}

//updatable = false
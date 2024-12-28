package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Packages extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "categories_id")
    private Integer categoriesId;


    @Column(name = "deposit_percent")
    private Integer depositPercent; // Thêm trường tiền cọc


    @Column(name = "i18n_language_id")
    private Integer i18nLanguageId;
}

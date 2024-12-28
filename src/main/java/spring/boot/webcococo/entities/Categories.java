package spring.boot.webcococo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "i18n_language_id")
    private Integer i18nLanguageId;

}

//updatable = false
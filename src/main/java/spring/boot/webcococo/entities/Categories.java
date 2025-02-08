package spring.boot.webcococo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        name = "categories"
//        , uniqueConstraints = @UniqueConstraint(columnNames = {"name_translation_key_id", "parent_id","id"})
)
public class Categories extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_translation_key_id")
    private Integer nameTranslationKeyId;

    @Column(name = "is_active_translation_key_id")
    private Integer isActiveTranslationKeyId;

    @Column(name = "parent_id")
    private Integer parentId;


    @Column(name = "path")
    private Integer path;

    @Column(name = "image")
    private String image;

}

//updatable = false
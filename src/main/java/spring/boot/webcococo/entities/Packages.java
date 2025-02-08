package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "packages" , uniqueConstraints = @UniqueConstraint(columnNames =  {"name_translation_key_id","description_translation_key_id","categories_id"}))
public class Packages extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_translation_key_id")
    private Integer nameTranslationKeyId;

    @Column(name = "description_translation_key_id")
    private Integer descriptionTranslationKeyId;

    @Column(name = "categories_id")
    private Integer categoriesId;





}

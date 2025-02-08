package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "translation_key")
@Entity
public class TranslationKey  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "`key`", nullable = false, unique = true) // Dùng backtick bao quanh 'key'
    private String key;


    @NotNull
    @Column(name = "model_id", nullable = false) // Khóa phải là duy nhất
    private Integer modelId;


    @Column(name = "description")
    private String description;
}

package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "translation",  uniqueConstraints = @UniqueConstraint(columnNames = {"translation_key_id", "i18n_language_id"})
)
@Entity
public class Translation {

    @Id
    private String id = UUID.randomUUID().toString(); // Tự động sinh UUID cho mỗi bản ghi

    @NotNull
    @Column(name = "translation_key_id", nullable = false) // Đảm bảo không null trong cơ sở dữ liệu
    private Integer translationKeyId;

    @NotNull
    @Column(name = "i18n_language_id", nullable = false) // Đảm bảo không null trong cơ sở dữ liệu
    private Integer i18nLanguageId;






    @Column(name = "content") // Đảm bảo không null trong cơ sở dữ liệu
    private String content;


}

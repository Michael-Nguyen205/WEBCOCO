package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CategoriesRequest {
    private Integer id; // ID của danh mục (có thể null nếu là danh mục mới)
    private String name; // Tên của danh mục
    @JsonProperty("parent_id")
    private Long parentId; // ID của danh mục cha (null nếu là root)

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("i18n_language_id")
    private Integer i18nLanguageId;

    private List<CategoriesRequest> children; // Danh sách các danh mục con
}

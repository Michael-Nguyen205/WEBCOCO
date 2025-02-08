package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriesResponse {
    private Integer id; // ID của danh mục
    private String name; // Tên danh mục
    @JsonProperty("parent_id")
    private Integer parentId; // ID của danh mục cha (null nếu là danh mục gốc)
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("i18n_language_id")
    private Integer i18nLanguageId;
    @JsonProperty("language_code")
    private String languageCode;
    private List<CategoriesResponse> children; // Danh sách các danh mục con
}

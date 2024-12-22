package spring.boot.webcococo.models.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriesPojo {
    private Integer id; // ID của danh mục (có thể null nếu là danh mục mới)
    private String name; // Tên của danh mục
    private Integer parentId; // ID của danh mục cha (null nếu là root)
    private Integer level; // Danh sách các danh mục con
}

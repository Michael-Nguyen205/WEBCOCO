package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoriesResponse {
    private Integer id; // ID của danh mục
    private String name; // Tên danh mục
    private Integer parentId; // ID của danh mục cha (null nếu là danh mục gốc)
    private List<CategoriesResponse> children; // Danh sách các danh mục con
}

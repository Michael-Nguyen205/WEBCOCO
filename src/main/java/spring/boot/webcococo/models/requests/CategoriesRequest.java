package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long parentId; // ID của danh mục cha (null nếu là root)
    private List<CategoriesRequest> children; // Danh sách các danh mục con
}

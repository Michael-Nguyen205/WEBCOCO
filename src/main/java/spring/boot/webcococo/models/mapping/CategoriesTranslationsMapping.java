package spring.boot.webcococo.models.mapping;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesTranslationsMapping {
    // Thông tin danh mục
    private Integer categoryId;           // c_id
    private Integer parentCategoryId;     // c_parent_id

    // Thông tin ngôn ngữ
    private Integer languageId;           // l_id

    private String languageCode;          // l_code

    // Thông tin name translation
    private Integer nameTranslationKeyId; // tk_name_id
    private String nameTranslationKey;    // tk_name_key
    private String nameTranslationId;     // t_name_id
    private String nameTranslationContent; // t_name_content

    // Thông tin is_active translation
    private Integer isActiveTranslationKeyId; // tk_is_active_id
    private String isActiveTranslationKey;    // tk_is_active_key
    private String isActiveTranslationId;     // t_is_active_id
    private String isActiveTranslationContent; // t_is_active_content


}

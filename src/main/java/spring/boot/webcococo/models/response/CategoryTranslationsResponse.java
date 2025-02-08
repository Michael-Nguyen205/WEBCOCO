package spring.boot.webcococo.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTranslationsResponse {
    private Integer id;
    private Integer parentId;
    private List<CategoryLanguage> languages;
    private List<CategoryTranslationsResponse> children;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CategoryLanguage {

        private Integer languageId;
        private String languageCode;
        private List<TranslationResponse> TranslationResponse;
//        private IsActiveTranslationFields isActiveTranslationFields;
    }
}

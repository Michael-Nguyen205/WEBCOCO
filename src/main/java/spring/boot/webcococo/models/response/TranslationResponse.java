package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationResponse {


    @JsonProperty("translation_key_id")
    private Integer translationKeyId;  // ID của khóa bản dịch

    @JsonProperty("translation_key")
    private String translationKey;     // Khóa bản dịch (ví dụ: "Categories.name.1")

    @JsonProperty("translation_id")
    private String translationId;      // ID bản dịch cụ thể

    @JsonProperty("translation_content")
    private Object translationContent; //
}

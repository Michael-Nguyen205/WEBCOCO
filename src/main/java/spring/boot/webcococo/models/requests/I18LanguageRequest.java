package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class I18LanguageRequest {

    @NotBlank(message = "Code cannot be blank")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;

    @Size(max = 20, message = "Locale must not exceed 20 characters")
    private String locale;

    @Size(max = 20, message = "Name must not exceed 20 characters")
    private String name;

    @Size(max = 100, message = "Native name must not exceed 100 characters")
    @JsonProperty("native_name")
    private String nativeName;

    @NotNull(message = "Active status must not be null")
    @JsonProperty("is_active")
    private Boolean isActive;
}

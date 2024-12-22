package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorizeResponse {


    @JsonProperty("Permisson")
    private PermissionResponse permissonResponse;
}

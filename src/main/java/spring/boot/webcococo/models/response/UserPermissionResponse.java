package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPermissionResponse {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("permission_id")
    private Integer permissionId;
}

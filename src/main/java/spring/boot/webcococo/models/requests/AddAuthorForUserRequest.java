package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAuthorForUserRequest {


    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("permission_id")
    private Integer permissionId;


}

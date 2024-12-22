package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginRequest {

    @NotBlank(message = "Email number is required")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Min(value = 1, message = "You must enter role's Id")
    @JsonProperty("permission_id")
    private Long permissionId;


}

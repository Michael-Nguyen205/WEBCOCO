package spring.boot.webcococo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "UsernName cannot be blank")
    private String usernName;

    private String name;

    @NotBlank(message = "Password cannot be blank")
    private String password;


    private List<String> roles;

}
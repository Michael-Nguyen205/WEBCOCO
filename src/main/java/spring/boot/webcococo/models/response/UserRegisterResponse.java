package spring.boot.webcococo.models.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponse {

    private String id;
    private String name;
    private String username;
    private String phoneNumber;
    private String email;


}

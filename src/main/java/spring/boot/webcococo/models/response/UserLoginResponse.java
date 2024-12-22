package spring.boot.webcococo.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private String token; // JWT token
    private List<String> permissionList;
    private UserResponse user; // Thông tin người dùng
}




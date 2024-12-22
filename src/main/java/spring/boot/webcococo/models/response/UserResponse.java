package spring.boot.webcococo.models.response;


import lombok.*;
import spring.boot.webcococo.entities.Users;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id; // ID của người dùng
    private String name; // Tên người dùng
    private String email; // Địa chỉ email của người dùng
    public static UserResponse toUserResponse(Users user) {
        if (user == null) {
            return null; // Trả về null nếu user là null
        }

        return UserResponse.builder()
                .id(user.getId().toString()) // Chuyển đổi ID thành String nếu cần
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}

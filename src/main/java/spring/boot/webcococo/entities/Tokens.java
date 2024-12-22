package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Tokens {
    @Id
//    @Builder.Default
    private String id ;

    @NotBlank(message = "Token cannot be blank") // Không cho phép token rỗng
    @Size(max = 500, message = "Token must be less than or equal to 300 characters") // Độ dài tối đa cho token
    @Column(name = "token")
    private String token;

    @Version
    private Integer version;


    @NotBlank(message = "Refresh token cannot be blank") // Không cho phép refresh token rỗng
    @Size(max = 500, message = "Refresh token must be less than or equal to 300 characters") // Độ dài tối đa cho refresh_token
    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;

    @Column(name = "is_mobile")
    private boolean isMobile;
    @Column(name = "revoked")
    private boolean revoked;
    @Column(name = "expired")
    private boolean expired;

    @Column(name = "user_id")
    private String userId;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

//    public Tokens() {
//        this.id = UUID.randomUUID().toString(); // Tạo UUID mới
//    }

}

package spring.boot.webcococo.models.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.function.ServerRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomWebAuthenticationDetails {
    private String userAgent; // Để không cần final, vì có thể sẽ khởi tạo sau
    private String ipAddress;
    private Object model; // Đối tượng khác mà bạn muốn lưu trữ

    // Constructor nhận ServerRequest để tự động khởi tạo thông tin
    public CustomWebAuthenticationDetails(ServerRequest request) {
        this.userAgent = request.headers().header("User-Agent").stream().findFirst().orElse("unknown");
        this.ipAddress = request.remoteAddress().map(address -> address.getAddress().getHostAddress()).orElse("unknown");
    }
}

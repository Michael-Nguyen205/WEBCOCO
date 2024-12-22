package spring.boot.webcococo.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


@Component
public class IpAddressUtil {

    // Phương thức lấy địa chỉ IP của client
    public  String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            // Kiểm tra xem có header "X-FORWARDED-FOR" hay không
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            
            // Nếu header không tồn tại hoặc rỗng, lấy địa chỉ IP từ remoteAddr
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            // Nếu có lỗi, gán giá trị IP là thông báo lỗi
            ipAddress = "Invalid IP: " + e.getMessage();
        }
        return ipAddress;
    }
}

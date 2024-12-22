package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BankGatewayDetailResponse {

    private Integer id;
    private String name;           // Tên cổng ngân hàng
    private String code; // URL cổng thanh toán ngân hàng
    private String paymentMehodName;

    private String gatewayUrl;
    private String secretKey;
    private String tmlCode;
    private String returnUrl;


}





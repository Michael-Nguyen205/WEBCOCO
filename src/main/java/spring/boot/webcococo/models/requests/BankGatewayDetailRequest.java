package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankGatewayDetailRequest {
    private Integer id;
    private String name;

    @JsonProperty("gateway_url")
    private String gatewayUrl;

    @JsonProperty("bank_code")
    private String bankCode;


    @JsonProperty("payment_method_id")
    private Integer paymentMehthodId;// URL cổng thanh toán ngân hàng
}

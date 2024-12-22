package spring.boot.webcococo.models.requests;

import lombok.Data;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodRequest {
    private Integer id;
    private String name;              // Tên phương thức thanh toán (id shop , chuyển khoản , tiền mặt )
    private String description;       // Mô tả phương thức thanh toán
    private List<BankGatewayDetailRequest> bankGatewayDetailRequests;
}

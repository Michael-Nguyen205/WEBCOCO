package spring.boot.webcococo.models.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
public class PaymentResponse {
    private String codeStatus;
    private String message;
    private String paymentUrl; // Có thể chung cho tất cả cổng thanh toán, nhưng nếu không thì có thể thay thế bằng các trường khác trong các cổng


    @SuperBuilder

    public static class VNPayResponse extends PaymentResponse {
    }

    @SuperBuilder
    public static class PayPalResponse extends PaymentResponse {
        private String approvalUrl; // Cổng PayPal có approvalUrl
    }

    @SuperBuilder
    public static class StripeResponse extends PaymentResponse {
        private String sessionId; // Cổng Stripe có sessionId
    }
}

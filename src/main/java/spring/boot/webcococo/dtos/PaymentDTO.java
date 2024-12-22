package spring.boot.webcococo.dtos;


import lombok.*;

@Data
@Builder

@Getter
@Setter
public class PaymentDTO {


    @Getter
    @Setter
    @Builder

    public static class PaymentVnpayDTO {
        private String orderId;
        private String paymentUrl;
        private String tmlCode;
        private String secretKey;
        private Integer amount;
        private String bankCode;
        private String ipAddress;
        private String cardType;
        private String returnUrl;

    }



    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentPaypalDTO {
        private Integer orderId;
        private Integer amount;
        private String bankCode;
        private String ipAddress;
        private String cardType;
    }

    // Getters, Setters, Builder methods
}
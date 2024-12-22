package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bank_gateway_details")
public class BankGatewayDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String gatewayUrl;

    @Column(name = "tml_code")
    private String tmlCode;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "return_url")
    private String returnUrl;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_gateway_id")
    private Integer bankGatewayId;


    // Getters and Setters
}

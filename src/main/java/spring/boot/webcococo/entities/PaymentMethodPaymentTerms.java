package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_method_payment_terms")
public class PaymentMethodPaymentTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @Column(name = "payment_terms_id")
    private Integer paymentTermsId;
}

package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class PaymentTerms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(name = "package_id")
    private Integer packagesId;

    @Column(name = "product_id")
    private Integer productId;

//    @Column(name = "payment_method_id")
//    private Integer paymentMethodId;

    @Column(name = "price_translation_key_id")
    private Integer priceTranslationKeyId;


    @Column(name = "deposit_percent_translation_key_id")
    private Integer depositPercentTranslationKeyId; // Thêm trường tiền cọc


    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;
}

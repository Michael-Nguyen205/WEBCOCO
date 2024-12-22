package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="payment_condition_on_monthly")

public class PaymentsConditionOnMonthly extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_terms_id")
    private Integer paymentTermsId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "duration_months")
    private Integer durationMonths;
}

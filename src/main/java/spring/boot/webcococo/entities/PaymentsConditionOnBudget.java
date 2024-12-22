package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="payment_condition_on_budget")

public class PaymentsConditionOnBudget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_terms_id")
    private Integer paymentTermsId;

    @Column(name = "min_budget")
    private Double minBudget;

    @Column(name = "max_budget")
    private Double maxBudget;

    @Column(name = "fixed_fee")
    private Double fixedFee;

    @Column(name = "percentage_fee")
    private Double percentageFee;
}

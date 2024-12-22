package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_method")
public class PaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @Column(name = "is_active")
    private Boolean idActive;

//    @Column(name = "bank_gateway_id")
//    private Integer bankGatewayId;

    // Getters and Setters
}

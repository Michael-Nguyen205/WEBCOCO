package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "payment_transaction")
public class PaymentTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;



    @Column(name = "bank_gateway_detail_id")
    private Integer bankGatewayDetailId;

    @Column(name = "transaction_code", length = 255)
    private String transactionCode;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 100)
    private String currency;

    @Column(name = "status", nullable = false, length = 50)
    private PaymentTransactionStatus status;



    public enum PaymentTransactionStatus {

        PAID,           // Đã thanh toán: User đã thanh toán đầy đủ.
        COMPLETED,      // Đã hoàn thành: Đơn hàng đã xử lý xong và giao dịch hoàn tất.
        FAILED,         // Thất bại
        OVERDUE,        // Quá hạn

    }
}


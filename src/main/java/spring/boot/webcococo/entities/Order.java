package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "deposit_amount", nullable = false)
    private BigDecimal depositAmount; // Số tiền cần cọc

    @Column(name = "paid_amount", nullable = false)
    private BigDecimal PaidAmount; // Tổng số tiền người dùng đã thanh toan

    @Column(name = "bank_gateway_detail_id", nullable = false)
    private Integer bankGateWayDetailId; // Tổng số tiền người dùng đã thanh toan



    //    @Column(name = "due_date", nullable = true)
//    private LocalDateTime dueDate;
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong CSDL
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus; // Trạng thái chi tiết đơn hàng

    // Enum định nghĩa các trạng thái
    public enum OrderStatus {
        PENDING,        // Đang chờ xử lý
        APPROVED,       // Đã được duyệt: Admin đã duyệt đơn hàng, user có thể tiến hành thanh toán hoặc đặt cọc.
        DEPOSITED,      // Đã đặt cọc: User đã đặt cọc một phần số tiền.
        PAID,           // Đã thanh toán: User đã thanh toán đầy đủ.
        COMPLETED,      // Đã hoàn thành: Đơn hàng đã xử lý xong và giao dịch hoàn tất.
        FAILED,         // Thất bại
        OVERDUE,        // Quá hạn

    }

}

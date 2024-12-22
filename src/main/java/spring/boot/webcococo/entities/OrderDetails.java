package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetails extends spring.boot.webcococo.entities.BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "packages_id")
    private Integer packagesId;

    @Column(name = "order_id")
    private Integer orderId;

    private Integer quantity = 1;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // Getters and Setters



//    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong CSDL
//    @Column(name = "order_detail_status", nullable = false)
//    private OrderDetailStatus orderDetailStatus; // Trạng thái chi tiết đơn hàng
//
//    // Enum định nghĩa các trạng thái
//    public enum OrderDetailStatus {
//        PENDING, COMPLETED, FAILED, UPDATEORDER, OVERDUE
//    }



}

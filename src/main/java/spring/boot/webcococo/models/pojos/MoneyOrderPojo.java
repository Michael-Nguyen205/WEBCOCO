package spring.boot.webcococo.models.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.boot.webcococo.entities.OrderDetails;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyOrderPojo {

    private BigDecimal totalPrice; // Tổng giá trị đơn hàng
    private BigDecimal depositAmount; // Số tiền cọc

    private List<OrderDetails> orderDetailsList; // Số tiền cọc
 // Số tiền cọc

    // Constructor, getters, setters sẽ được Lombok tự động sinh ra nhờ các annotation trên
}

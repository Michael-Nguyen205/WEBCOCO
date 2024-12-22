package spring.boot.webcococo.models.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyItemPojo {

// Tổng giá trị đơn hàng
    private BigDecimal totalItemPrice; // Tổng giá trị đơn hàng
    private Integer totalDepositPercent; // Số tiền cọc

    // Constructor, getters, sesentters sẽ được Lombok tự động sinh ra nhờ các annotation trên
}

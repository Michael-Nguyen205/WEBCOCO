package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import spring.boot.webcococo.entities.PaymentsConditionOnMonthly;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2
public class PaymentConditionOnMonthlyResponse {

    private Integer id;

    private BigDecimal price;

    @JsonProperty("duration_months")
    private Integer durationMonths;




    public static Set<PaymentConditionOnMonthlyResponse> toPaymentConditionOnMonthlyResponse(Set<PaymentsConditionOnMonthly> paymentsConditionOnMonthlies) {
        // Sử dụng Builder nếu cần
        Set<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses = new HashSet<>();
        for (PaymentsConditionOnMonthly paymentsConditionOnMonthly : paymentsConditionOnMonthlies ){


            log.error("paymentsConditionOnMonthly:{}",paymentsConditionOnMonthly);

            PaymentConditionOnMonthlyResponse paymentConditionOnMonthlyResponse = PaymentConditionOnMonthlyResponse.builder()
                    .id(paymentsConditionOnMonthly.getId())
                    .price(paymentsConditionOnMonthly.getPrice())
                    .durationMonths(paymentsConditionOnMonthly.getDurationMonths())
                    .build();

            paymentConditionOnMonthlyResponses.add(paymentConditionOnMonthlyResponse);
        }

        // Thiết lập thời gian tạo và cập nhật


        return paymentConditionOnMonthlyResponses;
    }

}

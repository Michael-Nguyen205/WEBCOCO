package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.PaymentsConditionOnBudget;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PaymentConditionOnBudgetResponse {

    @JsonProperty("min_budget")
    private Double minBudget;


    @JsonProperty("max_budget")
    private Double maxBudget;


    @JsonProperty("fixed_fee")
    private Double fixedFee;


    @JsonProperty("percentage_fee")
    private Double percentageFee;

    public static Set<PaymentConditionOnBudgetResponse> toPaymentConditionOnBudgetResponse(Set<PaymentsConditionOnBudget> paymentsConditionOnBudgetList) {
        // Sử dụng Builder nếu cần
        Set<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponses = new HashSet<>();
        for (PaymentsConditionOnBudget paymentsConditionOnBudget : paymentsConditionOnBudgetList ){
            PaymentConditionOnBudgetResponse paymentConditionOnBudgetResponse = PaymentConditionOnBudgetResponse.builder()
                    .maxBudget(paymentsConditionOnBudget.getMaxBudget())
                    .minBudget(paymentsConditionOnBudget.getMinBudget())
                    .fixedFee(paymentsConditionOnBudget.getFixedFee())
                    .percentageFee(paymentsConditionOnBudget.getPercentageFee())
                    .build();

            paymentConditionOnBudgetResponses.add(paymentConditionOnBudgetResponse);
        }


        // Thiết lập thời gian tạo và cập nhật


        return paymentConditionOnBudgetResponses;
    }
}

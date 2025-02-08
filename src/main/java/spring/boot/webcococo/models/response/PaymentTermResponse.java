package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.PaymentTerms;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)

public class PaymentTermResponse {


//    private String name;

    @JsonProperty("packages_id")
    private Integer packagesId;

    @JsonProperty("product_id")
    private Integer productId;

    private BigDecimal price;

    @JsonProperty("deposit_percent")
    private Integer depositPercent;


    @JsonProperty("payment_condition_on_budget")
    private Set<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponses;

    @JsonProperty("payment_condition_on_monthly")
    private Set<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses;








    public static PaymentTermResponse toPayMentTermResponse(PaymentTerms paymentTerms, Set<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponseList, Set<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses){

            PaymentTermResponse response = new PaymentTermResponse();
//            response.setName(paymentTerms.getName());

            response.setPaymentConditionOnBudgetResponses(paymentConditionOnBudgetResponseList);
            response.setPaymentConditionOnMonthlyResponses(paymentConditionOnMonthlyResponses);
            return response;

    }


}

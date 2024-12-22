package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class PaymentTermRequest {


    private String name;

//    @JsonProperty("packages_id")
//    private Integer packagesId;
//
//    @JsonProperty("product_id")
//    private Integer productId;

    @JsonProperty("payment_method_id")
    private Integer paymentMethodId;

    private BigDecimal price;

    @JsonProperty("start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @JsonProperty("end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    @JsonProperty("payment_condition_on_budget")
    private Set<PaymentConditionOnBudgetRequest> paymentConditionOnBudgetRequestList;

    @JsonProperty("payment_condition_on_monthly")
    private Set<PaymentConditionOnMonthlyRequest> paymentConditionOnMonthlyRequestList;

//    private List<PaymentMethodRequest> paymentMethodRequests;
    @JsonProperty("payment_method_id_list")
    private Set<Integer> paymentMethoIdList;


}

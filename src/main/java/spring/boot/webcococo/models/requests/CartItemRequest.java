package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CartItemRequest {

    @JsonProperty("productId")
    private Integer productId;


    @JsonProperty("packageId")
    private Integer packageId;


    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;





    @JsonProperty("payment_condition_on_monthly_id")
    private Integer paymentConditionOnMonthlyId;


    @JsonProperty("payment_condition_on_budget_id")
    private Integer paymentConditionBudgetId;




}

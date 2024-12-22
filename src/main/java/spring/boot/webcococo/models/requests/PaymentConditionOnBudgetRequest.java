package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentConditionOnBudgetRequest  {

    @JsonProperty("min_budget")
    private Double minBudget;


    @JsonProperty("max_budget")
    private Double maxBudget;


    @JsonProperty("fixed_fee")
    private Double fixedFee;


    @JsonProperty("percentage_fee")
    private Double percentageFee;
}

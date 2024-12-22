package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class PaymentConditionOnMonthlyRequest {



    private BigDecimal price;


    @JsonProperty("duration_months")
    private Integer durationMonths;
}

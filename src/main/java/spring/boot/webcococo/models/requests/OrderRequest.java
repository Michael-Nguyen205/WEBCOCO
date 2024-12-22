package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {

    @NotNull(message = "User ID cannot be null")
    @JsonProperty("user_id")
    private String userId;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @Size(max = 255, message = "Note should not exceed 255 characters")
    private String note;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("depositAmout")
    private BigDecimal depositAmount;

    @NotNull(message = "Cart items cannot be null")
    @JsonProperty("cart_items")
    private List<CartItemRequest> cartItems;



    @NotNull(message = "payment_method items cannot be null")
    @JsonProperty("payment_method")
    private PaymentMethodRequest paymentMethodRequest;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PaymentMethodRequest {

        @JsonProperty("id")
        @NotNull(message = "Payment Method ID cannot be null")
        private Integer id;

        @JsonProperty("bank_gateway_id")
        private Integer bankGatewayId;


        @JsonProperty("bank_gateway_details_id")
        private Integer bankGatewayDetailId;

        // Getters and Setters
    }

    // Getters and Setters
}

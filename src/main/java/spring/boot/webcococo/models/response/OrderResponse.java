package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.Order;
import spring.boot.webcococo.entities.OrderDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    @JsonProperty("order_id")
    private Integer orderId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("note")
    private String note;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty( "deposit_amount")
    private BigDecimal depositAmount; // Số tiền cần cọc

    @JsonProperty("paid_amount")
    private BigDecimal paidAmount; // Tổng số tiền người dùng đã thanh toan



    @JsonProperty("order_detail")
    private List<OrderDetailResponse> orderDetail;

    @JsonProperty("payment_method")
    private PaymentMethodResponse paymentMethod;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PaymentMethodResponse {

        @JsonProperty("payment_method_name")
        private String paymentMethodName;

        @JsonProperty("bank_gate_way_name")
        private String bankGateWayName;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
//    @JsonInclude(JsonInclude.Include.NON_NULL)

    public static class OrderDetailResponse {

        @JsonProperty("product_name")
        private Integer productId;

        @JsonProperty("package_name")
        private Integer packageId;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("price")
        private BigDecimal totalPrice;

    }



    public static OrderResponse toOrderResponse(Order order, List<OrderDetails> orderDetailsList) {
        // Chuyển đổi orderDetailsList thành một danh sách OrderDetailResponse
        List<OrderDetailResponse> orderDetailResponses = orderDetailsList.stream()
                .map(orderDetail -> new OrderDetailResponse(
                        orderDetail.getProductId(),
                        orderDetail.getPackagesId(),
                        orderDetail.getQuantity(),
                        orderDetail.getTotalPrice())
                ).toList();// Chuyển đổi thành danh sách

        // Tạo đối tượng OrderResponse và trả về
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .note(order.getNote())
                .totalPrice(order.getTotalPrice())
                .orderDetail(orderDetailResponses)
//                .paymentMethod(new PaymentMethodResponse(
//                        order.getPaymentMethodName(),
//                        order.getBankGateWayName()))
                .orderStatus(order.getOrderStatus().name())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }



}

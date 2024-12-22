package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.entities.BankGatewayDetail;
import spring.boot.webcococo.entities.PaymentMethod;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.BankGateWayRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PaymentMethodResponse {
//    private  ApplicationContext context;


    private String name;              // Tên phương thức thanh toán
    private String description;       // Mô tả phương thức thanh toán
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("bank_gateway_id")
    private Integer bankGatewayId;     // ID của cổng ngân hàng

    @JsonProperty("bank_gateways")
    private Set<BankGatewayDetailResponse> bankGatewayDetailRespons;     // ID của cổng ngân hàng






    public static PaymentMethodResponse toPaymentMethodResponse(List<BankGatewayDetail> bankGatewayDetails, PaymentMethod paymentMethod, BankGateWayRepository bankGateWayRepository
    ) {

        // Khởi tạo danh sách các BankGatewayResponse, đảm bảo không bị null
        List<BankGatewayDetailResponse> bankGatewayDetailRespons = new ArrayList<>();

        if (bankGatewayDetails != null && !bankGatewayDetails.isEmpty()) {
            for ( BankGatewayDetail bankGatewayDetail : bankGatewayDetails){
                BankGatewayDetailResponse bankGatewayDetailResponse = new BankGatewayDetailResponse();
                BankGateWay bankGateWay = bankGateWayRepository.findById(bankGatewayDetail.getBankGatewayId()).orElseThrow(()-> new AppException( ErrorCodeEnum.DATA_NOT_FOUND));
                String bankName = bankGateWay.getName();
                bankGatewayDetailResponse.setId(bankGatewayDetail.getId());
                bankGatewayDetailResponse.setName(bankName);
                bankGatewayDetailResponse.setGatewayUrl(bankGatewayDetail.getGatewayUrl());
                bankGatewayDetailResponse.setCode(bankGatewayDetail.getBankCode());
                bankGatewayDetailRespons.add(bankGatewayDetailResponse);
            }
        }

        // Tránh NullPointerException bằng cách kiểm tra paymentMethod trước khi truy cập thuộc tính của nó
        String name = paymentMethod != null ? paymentMethod.getName() : null;
        String description = paymentMethod != null ? paymentMethod.getDescription() : null;

        // Tránh lỗi khi sử dụng stream() trên null
        Set<BankGatewayDetailResponse> bankGatewayDetailResponseSet = new HashSet<>(bankGatewayDetailRespons);

        return PaymentMethodResponse.builder()
                .name(name)
                .description(description)
                .isActive(paymentMethod.getIdActive())
                .bankGatewayDetailRespons(bankGatewayDetailResponseSet)
                .build();
    }


}


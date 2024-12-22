package spring.boot.webcococo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.configurations.VnpayConfig;
import spring.boot.webcococo.dtos.PaymentDTO;
import spring.boot.webcococo.models.response.PaymentResponse;
import spring.boot.webcococo.utils.VNPayUtil;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VnpayConfig vnPayConfig;


    public PaymentResponse.VNPayResponse createVnPayPaymentUrl(PaymentDTO.PaymentVnpayDTO paymentDTO) {
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_TxnRef", paymentDTO.getOrderId());
        vnpParamsMap.put("vnp_Amount", String.valueOf(paymentDTO.getAmount()));
        vnpParamsMap.put("vnp_TmnCode", paymentDTO.getTmlCode());
        vnpParamsMap.put("vnp_ReturnUrl", paymentDTO.getReturnUrl());


        if (paymentDTO.getBankCode() != null && !paymentDTO.getBankCode().isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", paymentDTO.getBankCode());
        }
        if (paymentDTO.getCardType() != null) {
            vnpParamsMap.put("vnp_CardType", paymentDTO.getCardType());
        }

        vnpParamsMap.put("vnp_IpAddr", paymentDTO.getIpAddress());

//        vnp_CardType QRCODE

        log.error("paymentDTO.getSecretKey():{}",paymentDTO.getSecretKey());

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(paymentDTO.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = paymentDTO.getPaymentUrl() + "?" + queryUrl;
        return PaymentResponse.VNPayResponse.builder()
                .codeStatus("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }


    public PaymentResponse.PayPalResponse createPaypalPayment(PaymentDTO.PaymentPaypalDTO paymentDTO) {

        return null;
    }


}

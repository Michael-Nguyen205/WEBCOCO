package spring.boot.webcococo.services.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.configurations.VnpayConfig;
import spring.boot.webcococo.dtos.PaymentDTO;
import spring.boot.webcococo.entities.Order;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.response.BankGatewayDetailResponse;
import spring.boot.webcococo.models.response.ExchangeRateResponse;
import spring.boot.webcococo.models.response.PaymentResponse;
import spring.boot.webcococo.repositories.OrderRepository;
import spring.boot.webcococo.repositories.https_client.ExchangerateClient;
import spring.boot.webcococo.services.IPaymentService;
import spring.boot.webcococo.utils.VNPayUtil;

import java.math.BigDecimal;
import java.util.Map;


@Log4j2
@Service

public class PaymentServiceImpl implements IPaymentService {

    private final OrderRepository orderRepository;
    private final VnpayConfig vnPayConfig;
    private final ExchangerateClient exchangerateClient;
    private final BankGateWayDetailServiceImpl bankGateWayDetailService;


    public PaymentServiceImpl(OrderRepository orderRepository, ExchangerateClient exchangerateClient, BankGateWayDetailServiceImpl bankGateWayDetailService) {
        this.orderRepository = orderRepository;
        this.exchangerateClient = exchangerateClient;
        this.bankGateWayDetailService = bankGateWayDetailService;
        vnPayConfig = null;
    }


    @Override
    @Transactional
    public PaymentResponse paymentOnlineOrder(Integer orderId, String ipAddress) {
        try {
            // Kiểm tra null cho orderId và ipAddress
            if (orderId == null || ipAddress == null ) {
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "Order ID or IP ,bankGateWayDetalId Address is null");
            }

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Order not found with ID: " + orderId));


            BankGatewayDetailResponse bankGatewayDetailResponse = bankGateWayDetailService
                    .getBankgateWayDetail(order.getBankGateWayDetailId());

            if (bankGatewayDetailResponse == null) {
                throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Bank Gateway Detail not found");
            }

            order.setBankGateWayDetailId(bankGatewayDetailResponse.getId());
            log.error("order:{}", order);
            BigDecimal paymentAmount = (order.getDepositAmount() == null ||
                    order.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0)
                    ? order.getTotalPrice()
                    : order.getDepositAmount();


            if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("totalPrice:{}", order.getTotalPrice());
                log.error("paymentAmount:{}", paymentAmount);
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "Payment amount is invalid for Order ID: " + orderId);
            }

            return callPaymentRequest(bankGatewayDetailResponse, ipAddress, paymentAmount, order.getId());

        } catch (AppException ex) {
            // Xử lý riêng lỗi của ứng dụng
            throw ex;
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            throw ex;
        }
    }


    @Transactional
    protected PaymentResponse callPaymentRequest(BankGatewayDetailResponse bankGatewayDetailResponse, String ipAddress, BigDecimal paymentAmount, Integer orderId) {
        PaymentResponse paymentResponse = null;

        try {
            if (bankGatewayDetailResponse == null || ipAddress == null || paymentAmount == null) {
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "Invalid input parameters for payment request");
            }

            String paymentMethodName = bankGatewayDetailResponse.getPaymentMehodName();
            if (paymentMethodName == null || paymentMethodName.isEmpty()) {
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "Payment method name is missing in Bank Gateway Detail");
            }

            switch (paymentMethodName) {
                case "PAYMENTBANK": {
                    String bankName = bankGatewayDetailResponse.getName();
                    if (bankName == null || bankName.isEmpty()) {
                        throw new AppException(ErrorCodeEnum.NULL_POINTER, "Bank name is missing in Bank Gateway Detail");
                    }

                    switch (bankName) {
                        case "VNPAY": {
                            // Lấy tỷ giá của VND từ conversionRates
                            ExchangeRateResponse exchangeRateResponse = exchangerateClient.getExchangeRate();
                            Double vndRate = exchangeRateResponse.getConversionRates().get("VND");
                            log.error("vndRate:{}", vndRate);
                            paymentAmount = paymentAmount.multiply(BigDecimal.valueOf(vndRate));


                            PaymentDTO.PaymentVnpayDTO paymentVnpayDTO = PaymentDTO.PaymentVnpayDTO.builder()
                                    .orderId(orderId.toString())
                                    .amount(paymentAmount.intValue()) // Chuyển đổi paymentAmount sang kiểu int
                                    .paymentUrl(bankGatewayDetailResponse.getGatewayUrl())
                                    .secretKey(bankGatewayDetailResponse.getSecretKey())
                                    .tmlCode(bankGatewayDetailResponse.getTmlCode())
                                    .bankCode(bankGatewayDetailResponse.getCode()) // Lấy mã ngân hàng từ bankGatewayDetail
                                    .ipAddress(ipAddress) // Đặt địa chỉ IP
                                    .cardType(null) // Thiết lập cardType là null
                                    .returnUrl(bankGatewayDetailResponse.getReturnUrl())
                                    .build(); // Xây dựng đối tượng PaymentVnpayDTO

                            paymentResponse = createVnPayPaymentUrl(paymentVnpayDTO);
                            break;
                        }
                        case "PAYPAL": {
                            // Xử lý cho PayPal
                            // Thêm đoạn mã cụ thể ở đây
                            break;
                        }
                        default:
                            throw new AppException(ErrorCodeEnum.NULL_POINTER, "Unsupported bank name: " + bankName);
                    }
                    break;
                }
                case "QRCODE": {
                    String qrCodeName = bankGatewayDetailResponse.getName();
                    if (qrCodeName == null || qrCodeName.isEmpty()) {
                        throw new AppException(ErrorCodeEnum.NULL_POINTER, "QR Code name is missing in Bank Gateway Detail");
                    }

                    switch (qrCodeName) {
                        case "VNPAY": {
                            // Xử lý cho VNPAY
                            break;
                        }
                        case "PAYPAL": {
                            // Xử lý cho PAYPAL
                            break;
                        }
                        default:
                            throw new AppException(ErrorCodeEnum.NULL_POINTER, "Unsupported QR Code name: " + qrCodeName);
                    }
                    break;
                }
                default:
                    throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "Unsupported payment method: " + paymentMethodName);
            }

            if (paymentResponse == null) {
                throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "Payment response is null");
            }

            return paymentResponse;

        } catch (AppException ex) {
            // Xử lý riêng lỗi của ứng dụng
            throw ex;
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            throw ex;
        }
    }












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

package spring.boot.webcococo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import spring.boot.webcococo.entities.Order;
import spring.boot.webcococo.entities.PaymentTransaction;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.services.PaymentService;
import spring.boot.webcococo.services.impl.OrderServiceImpl;
import spring.boot.webcococo.services.impl.PaymentTransactionServiceImpl;

import java.math.BigDecimal;

@Log4j2
@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderServiceImpl orderService;

    private final PaymentTransactionServiceImpl paymentTransactionService;

//    @GetMapping("/vn-pay")
//    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
//        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
//    }


    @GetMapping("/vn-pay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        try {
            // Lấy tham số từ VNPay
            String currency = request.getParameter("vnp_CurrCode");
            String orderId = request.getParameter("vnp_TxnRef");
            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionNo = request.getParameter("vnp_TransactionNo");
            String amount = request.getParameter("vnp_Amount");
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String secureHash = request.getParameter("vnp_SecureHash");

            // Log thông tin giao dịch
            log.info("VNPay Callback received - Order ID: {}, Response Code: {}, Transaction No: {}, Amount: {}, Currency: {}",
                    orderId, responseCode, transactionNo, amount, currency);

            // Kiểm tra mã phản hồi
            if ("00".equals(responseCode)) {
                // Lấy thông tin đơn hàng
                Order order = orderService.findById(Integer.valueOf(orderId))
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Order not found"));

                // Cập nhật trạng thái đơn hàng
                if (order.getDepositAmount() == null) {
                    order.setOrderStatus(Order.OrderStatus.PAID);
                } else {
                    order.setOrderStatus(Order.OrderStatus.DEPOSITED);
                }

                // Lưu thông tin giao dịch
                paymentTransactionService.createPaymentTransaction(
                        Integer.valueOf(orderId),
                        transactionNo,
                        BigDecimal.valueOf(Long.parseLong(amount)),
                        currency,
                        PaymentTransaction.PaymentTransactionStatus.COMPLETED
                );

                // Chuyển hướng thành công
                return new RedirectView("https://www.nike.com/vn/");
            } else {
                // Chuyển hướng khi giao dịch thất bại
                log.error("Payment failed - Order ID: {}, Response Code: {}", orderId, responseCode);
                return new RedirectView("https://example.com/failure");
            }

        } catch (AppException e) {
            // Xử lý lỗi liên quan đến logic ứng dụng
            log.error("Application error during VNPay callback: {}", e.getMessage(), e);
            return new RedirectView("https://example.com/failure?error=app_error");
        } catch (NumberFormatException e) {
            // Xử lý lỗi khi chuyển đổi dữ liệu không hợp lệ
            log.error("Invalid number format in VNPay callback: {}", e.getMessage(), e);
            return new RedirectView("https://example.com/failure?error=invalid_data");
        } catch (Exception e) {
            // Bắt các lỗi không mong muốn khác
            log.error("Unexpected error during VNPay callback: {}", e.getMessage(), e);
            return new RedirectView("https://example.com/failure?error=unknown_error");
        }
    }



}

package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.dtos.PaymentDTO;
import spring.boot.webcococo.entities.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.MoneyItemPojo;
import spring.boot.webcococo.models.pojos.MoneyOrderPojo;
import spring.boot.webcococo.models.requests.CartItemRequest;
import spring.boot.webcococo.models.requests.OrderRequest;
import spring.boot.webcococo.models.response.*;
import spring.boot.webcococo.repositories.*;
import spring.boot.webcococo.repositories.https_client.ExchangerateClient;
import spring.boot.webcococo.services.EmailService;
import spring.boot.webcococo.services.IOrderService;
import spring.boot.webcococo.services.PaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Integer, OrderRepository> implements IOrderService {

    private final UserRepository userRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final PaymentConditionOnMonthlyRepository paymentConditionOnMonthlyRepository;
    private final PaymentConditionOnBudgetRepository paymentConditionOnBudgetRepository;
    private final PackagesRepository packagesRepository;

    private final PaymentService paymentService;

    private final BankGateWayRepository bankGateWayRepository;

    private final BankGatewayDetailRepository bankGatewayDetailRepository;


    private final PaymentMethodRepository paymentMethodRepository;


    private final PaymentTermsRepository paymentTermsRepository;

    private final EmailService emailService;

    private final BankGateWayDetailServiceImpl bankGateWayDetailService;

    private final ExchangerateClient exchangerateClient;


    // Ví dụ các phương thức giả lập để lấy giá package và product

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, OrderDetailsRepository orderDetailsRepository, PaymentConditionOnMonthlyRepository paymentConditionOnMonthlyRepository, PaymentConditionOnBudgetRepository paymentConditionOnBudgetRepository, PackagesRepository packagesRepository, PaymentService paymentService, BankGateWayRepository bankGateWayRepository, BankGatewayDetailRepository bankGatewayDetailRepository, PaymentMethodRepository paymentMethodRepository, PaymentTermsRepository paymentTermsRepository, EmailService emailService, BankGateWayDetailServiceImpl bankGateWayDetailService, ExchangerateClient exchangerateClient) {
        super(orderRepository); // Truyền repository vào lớp cha
        this.userRepository = userRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.paymentConditionOnMonthlyRepository = paymentConditionOnMonthlyRepository;
        this.paymentConditionOnBudgetRepository = paymentConditionOnBudgetRepository;
        this.packagesRepository = packagesRepository;
        this.paymentService = paymentService;
        this.bankGateWayRepository = bankGateWayRepository;
        this.bankGatewayDetailRepository = bankGatewayDetailRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentTermsRepository = paymentTermsRepository;
        this.emailService = emailService;
        this.bankGateWayDetailService = bankGateWayDetailService;
        this.exchangerateClient = exchangerateClient;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String ipAdress) {
        try {
            MoneyOrderPojo moneyOrderPojo = new MoneyOrderPojo();
            // Khởi tạo đối tượng Order
            Order order = new Order();
            Users users = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            order.setUserId(users.getId());
            // Gán email và số điện thoại
            order.setEmail(request.getEmail() != null ? request.getEmail() : users.getEmail());
            order.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : users.getPhoneNumber());
            order.setNote(request.getNote() != null ? request.getNote() : "");
            // Gán trạng thái đơn hàng
            order.setOrderStatus(Order.OrderStatus.PENDING);
            // số tiền user đã trả hiện tại là 0
            order.setPaidAmount(BigDecimal.ZERO);
            // nếu đây là order admin tự tạo
            if (request.getTotalPrice() != null && request.getDepositAmount() != null) {
                log.error("admin tạo order");
                save(order);
                moneyOrderPojo = createOrderDetail(request.getCartItems(), order, true);
                order.setTotalPrice(request.getTotalPrice());
                order.setDepositAmount(request.getDepositAmount());
            } else {
                log.error("khach tạo order");
                save(order);

                if (request.getCartItems() == null) {
                    log.error("getCartItems iss null ");
                    throw new AppException(ErrorCodeEnum.NULL_POINTER);
                }
                // Tạo chi tiết đơn hàng và tính toán giá trị đơn hàng
                moneyOrderPojo = createOrderDetail(request.getCartItems(), order, false);
                order.setDepositAmount(moneyOrderPojo.getDepositAmount());
                order.setTotalPrice(moneyOrderPojo.getTotalPrice());
                // Tạo thanh toán qua VNPAY
//                PaymentResponse paymentResponse = callPaymentRequest(request.getPaymentMethodRequest(), ipAdress, moneyOrderPojo.getDepositAmount());
            }
            // Lưu đơn hàng vào database
            save(order);

            List<OrderDetails> orderDetailsList = moneyOrderPojo.getOrderDetailsList();
            // Trả về phản hồi thành công
            return OrderResponse.toOrderResponse(order, orderDetailsList);
        } catch (AppException e) {
            // Xử lý lỗi liên quan đến dữ liệu không tìm thấy
            throw e;
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn khác
            throw e;
        }
    }


    @Transactional
    protected MoneyOrderPojo createOrderDetail(List<CartItemRequest> cartItemRequests, Order order, Boolean isAdminCreate) {
        // Log thông tin đầu vào
        log.error("cartItemRequests: {}", cartItemRequests);
        log.error("isAdminCreate: {}", isAdminCreate);
        log.error("order: {}", order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        BigDecimal totalPriceOrderPojo = BigDecimal.ZERO;
        BigDecimal depositAmountOrderPojo = BigDecimal.ZERO;
        Integer totalDepositPercentOrderPojo = 0;

        // Khởi tạo đối tượng MoneyOrderPojo để lưu thông tin tổng quan của đơn hàng
        MoneyOrderPojo moneyOrderPojo = new MoneyOrderPojo();

        try {
            // Duyệt qua từng CartItemRequest
            for (CartItemRequest cartItemRequest : cartItemRequests) {
                OrderDetails orderDetails = new OrderDetails();
                MoneyItemPojo moneyItemPojo = null;

                // Xử lý đơn hàng có Package
                if (cartItemRequest.getPackageId() != null && cartItemRequest.getProductId() == null) {
                    log.error("Xử lý đơn hàng có Package");

                    Packages packages = packagesRepository.findById(cartItemRequest.getPackageId())
                            .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

                    if (!isAdminCreate) {
                        moneyItemPojo = getPaymentPriceOnDepositPercentItem(cartItemRequest, packages);
                    }

                    // Lấy thông tin giá trị thanh toán và tiền cọc cho package
                    log.error("check null poiter:{}", moneyItemPojo.getTotalItemPrice());
                    orderDetails.setTotalPrice(moneyItemPojo.getTotalItemPrice());
                    orderDetails.setPackagesId(packages.getId());
                    orderDetails.setQuantity(cartItemRequest.getQuantity());
                    orderDetails.setOrderId(order.getId());
                }
                // Xử lý đơn hàng có Product
                else if (cartItemRequest.getPackageId() == null && cartItemRequest.getProductId() != null) {
                    log.error("Xử lý đơn hàng có Product");
                    // Thêm logic xử lý cho Product nếu cần
                }
                // Trường hợp dữ liệu không hợp lệ
                else {
                    throw new IllegalArgumentException("Invalid CartItemRequest: both packageId and productId are null or both are present.");
                }

                log.error("moneyItemPojo: {}", moneyItemPojo);

                // Cập nhật các giá trị tổng cộng cho tiền cọc và đơn hàng
                if (moneyItemPojo.getTotalDepositPercent() != null) {
                    totalDepositPercentOrderPojo += moneyItemPojo.getTotalDepositPercent();
                }
                totalPriceOrderPojo = totalPriceOrderPojo.add(moneyItemPojo.getTotalItemPrice());

                orderDetailsList.add(orderDetails);
            }

            // Tính toán số tiền cọc và số tiền còn lại cần phải thanh toán
            moneyOrderPojo.setDepositAmount(depositAmountOrderPojo);
            moneyOrderPojo.setTotalPrice(totalPriceOrderPojo);

            // Lưu tất cả OrderDetails vào cơ sở dữ liệu
            orderDetailsRepository.saveAll(orderDetailsList);
            moneyOrderPojo.setOrderDetailsList(orderDetailsList);

        } catch (AppException e) {
            log.error("AppException occurred: {}", e.getMessage(), e);
            throw e; // Ném lại ngoại lệ nếu cần để thông báo cho phía gọi
        } catch (IllegalArgumentException e) {
            log.error("Invalid data in CartItemRequest: {}", e.getMessage(), e);
            throw e; // Ném lại ngoại lệ nếu cần
        } catch (Exception e) {
            log.error("Unexpected exception occurred: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create order details due to unexpected error", e);
        }

        return moneyOrderPojo;
    }

    @Transactional
    protected MoneyItemPojo getPaymentPriceOnDepositPercentItem(CartItemRequest cardItem, Packages packages) {
        try {

            log.error("cardItem:{}", cardItem);
            log.error("depositPercentItem:{}", packages.getDepositPercent());


            Integer quantiy = cardItem.getQuantity();
            MoneyItemPojo moneyItemPojo = new MoneyItemPojo();
            //Condition Monthly Condition Monthly Condition Monthly Condition Monthly Condition Monthly
            if (cardItem.getPaymentConditionOnMonthlyId() != null && cardItem.getPaymentConditionBudgetId() == null) {
                // Nếu kiểu thanh toán là trả theo tháng kiểm tra nó tồn tại không và nó thuộc pakages này không
                PaymentsConditionOnMonthly paymentsConditionOnMonthly = paymentConditionOnMonthlyRepository.findById(cardItem.getPaymentConditionOnMonthlyId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
                Integer paymentTermId = paymentsConditionOnMonthly.getPaymentTermsId();
                PaymentTerms paymentTerms = paymentTermsRepository.findById(paymentTermId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
                Integer packageId = paymentTerms.getPackagesId();
                if (!Objects.equals(packages.getId(), packageId)) {
                    throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND);
                }


                if (packages.getDepositPercent() != null) {
                    //nêú có cọc
                    BigDecimal priceCondition = paymentsConditionOnMonthly.getPrice();
                    moneyItemPojo.setTotalDepositPercent(quantiy * packages.getDepositPercent());
                    moneyItemPojo.setTotalItemPrice(priceCondition.multiply(BigDecimal.valueOf(quantiy)));

                } else {
                    // không có cọc
                    BigDecimal priceCondition = paymentsConditionOnMonthly.getPrice();
                    moneyItemPojo.setTotalItemPrice(priceCondition.multiply(BigDecimal.valueOf(quantiy)));
                }
            } else if (cardItem.getPaymentConditionOnMonthlyId() == null && cardItem.getPaymentConditionBudgetId() != null) {
                // logic gì đó
            } else if (cardItem.getPrice() != null) {
                if (packages.getDepositPercent() != null) {
                    BigDecimal price = cardItem.getPrice();
                    moneyItemPojo.setTotalDepositPercent(quantiy * packages.getDepositPercent());
                    moneyItemPojo.setTotalItemPrice(price.multiply(BigDecimal.valueOf(quantiy)));
                    // không có cọc
                } else {
                    BigDecimal price = cardItem.getPrice();
                    moneyItemPojo.setTotalItemPrice(price.multiply(BigDecimal.valueOf(quantiy)));
                }
                // logic gì đó
            }
            return moneyItemPojo;
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public String approveOrder(String status, Integer orderId) {

        Order.OrderStatus statusOrder;
        try {
            statusOrder = Order.OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Invalid order status: " + status);
        }
        Order order = findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Order not found for ID: " + orderId));


        switch (statusOrder) {
            case APPROVED:
                if (order.getOrderStatus() == Order.OrderStatus.PENDING) {
                    order.setOrderStatus(Order.OrderStatus.APPROVED);
                    save(order);
                    EmailResponse emailResponse = emailService.sendPayEmail(order);
                    return "thanh cong gui payment email với id:" + emailResponse;
                } else {
                    throw new AppException(ErrorCodeEnum.INVALID_OPERATION, "Order is not in a state that can be approved.");
                }

            case COMPLETED:
                if (order.getOrderStatus() == Order.OrderStatus.APPROVED) {
                    order.setOrderStatus(Order.OrderStatus.COMPLETED);
                    save(order);
                    return "Order has been completed successfully.";
                } else {
                    throw new AppException(ErrorCodeEnum.INVALID_OPERATION, "Order must be approved before completing.");
                }

            default:
                throw new AppException(ErrorCodeEnum.INVALID_OPERATION, "Unsupported status update: " + statusOrder);
        }


    }






    
    @Override
    @Transactional
    public PaymentResponse payOrder(Integer orderId, String ipAddress, Integer bankGateWayDetalId) {
        try {
            log.error("bankGateWayDetalId:{}", bankGateWayDetalId);
            // Kiểm tra null cho orderId và ipAddress
            if (orderId == null || ipAddress == null || bankGateWayDetalId == null) {
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "Order ID or IP ,bankGateWayDetalId Address is null");
            }

            Order order = findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Order not found with ID: " + orderId));


            BankGatewayDetailResponse bankGatewayDetailResponse = bankGateWayDetailService
                    .getBankgateWayDetail(bankGateWayDetalId);

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

            return callPaymentRequest(bankGatewayDetailResponse, ipAddress, paymentAmount ,order.getId());

        } catch (AppException ex) {
            // Xử lý riêng lỗi của ứng dụng
            throw ex;
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            throw ex;
        }
    }

    @Transactional
    protected PaymentResponse callPaymentRequest(BankGatewayDetailResponse bankGatewayDetailResponse, String ipAddress, BigDecimal paymentAmount,Integer orderId) {
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
                            log.error("vndRate:{}",vndRate);
                            paymentAmount = paymentAmount.multiply( BigDecimal.valueOf( vndRate));


                            PaymentDTO.PaymentVnpayDTO paymentVnpayDTO = PaymentDTO.PaymentVnpayDTO.builder()
                                    .orderId(orderId.toString())
                                    .amount(paymentAmount.intValue()) // Chuyển đổi paymentAmount sang kiểu int
                                    .paymentUrl(bankGatewayDetailResponse.getGatewayUrl())
                                    .secretKey(bankGatewayDetailResponse.getSecretKey())
                                    .tmlCode(bankGatewayDetailResponse.getTmlCode())
                                    .bankCode(bankGatewayDetailResponse.getCode()) // Lấy mã ngân hàng từ bankGatewayDetail
                                    .ipAddress(ipAddress) // Đặt địa chỉ IP
                                    .cardType(null) // Thiết lập cardType là null
                                    .returnUrl(bankGatewayDetailResponse.getReturnUrl() )
                                    .build(); // Xây dựng đối tượng PaymentVnpayDTO

                            paymentResponse = paymentService.createVnPayPaymentUrl(paymentVnpayDTO);
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
}




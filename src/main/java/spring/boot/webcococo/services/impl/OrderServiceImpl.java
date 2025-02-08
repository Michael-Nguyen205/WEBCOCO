package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.dtos.PaymentDTO;
import spring.boot.webcococo.entities.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CustomUserDetails;
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
import java.math.RoundingMode;
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


    private  final  TranslationServiceImpl translationService;

    private  final  TranslationRepository translationRepository;

    private  final  I18nLanguageRepository i18nLanguageRepository;


    // Ví dụ các phương thức giả lập để lấy giá package và product

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, OrderDetailsRepository orderDetailsRepository, PaymentConditionOnMonthlyRepository paymentConditionOnMonthlyRepository, PaymentConditionOnBudgetRepository paymentConditionOnBudgetRepository, PackagesRepository packagesRepository, PaymentService paymentService, BankGateWayRepository bankGateWayRepository, BankGatewayDetailRepository bankGatewayDetailRepository, PaymentMethodRepository paymentMethodRepository, PaymentTermsRepository paymentTermsRepository, EmailService emailService, BankGateWayDetailServiceImpl bankGateWayDetailService, ExchangerateClient exchangerateClient, TranslationServiceImpl translationService, TranslationRepository translationRepository, I18nLanguageRepository i18nLanguageRepository) {
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
        this.translationService = translationService;
        this.translationRepository = translationRepository;
        this.i18nLanguageRepository = i18nLanguageRepository;
    }







    protected void  setBankGateWayDetailId(Order order , OrderRequest request){

        BankGatewayDetailResponse bankGatewayDetailResponse = bankGateWayDetailService
                .getBankgateWayDetail(request.getBankGateWayDetailId());
        order.setBankGateWayDetailId( bankGatewayDetailResponse.getId());


    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String ipAdress , String langCode) {
        try {
            MoneyOrderPojo moneyOrderPojo = new MoneyOrderPojo();
            // Khởi tạo đối tượng Order
            Order order = new Order();



            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

        // Kiểm tra nếu principal không phải là CustomUserDetails, hoặc userId không khớp
            if (!(principal instanceof CustomUserDetails customUserDetails) ||
                    !request.getUserId().equals(customUserDetails.getUser().getId())) {
                throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "Người dùng không hợp lệ");
            }

          // Lấy thông tin người dùng và gán userId
            Users users = customUserDetails.getUser();
            order.setUserId(users.getId());
            // Gán email và số điện thoại
            order.setEmail(request.getEmail() != null ? request.getEmail() : users.getEmail());
            order.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : users.getPhoneNumber());
            order.setNote(request.getNote() != null ? request.getNote() : "");
            // Gán trạng thái đơn hàng
            order.setOrderStatus(Order.OrderStatus.PENDING);
            // số tiền user đã trả hiện tại là 0
            order.setPaidAmount(BigDecimal.ZERO);
            setBankGateWayDetailId(order,request);


            // nếu đây là order admin tự tạo
            if (request.getTotalPrice() != null && request.getDepositAmount() != null) {
                log.error("admin tạo order");
                order.setTotalPrice(request.getTotalPrice());
                order.setDepositAmount(request.getDepositAmount());
                save(order);
                moneyOrderPojo = createOrderDetail(request.getCartItems(), order, true,langCode);
            } else {
                log.error("khach tạo order");
                save(order);

                if (request.getCartItems() == null) {
                    log.error("getCartItems iss null ");
                    throw new AppException(ErrorCodeEnum.NULL_POINTER);
                }
                // Tạo chi tiết đơn hàng và tính toán giá trị đơn hàng
                moneyOrderPojo = createOrderDetail(request.getCartItems(), order, false,langCode);
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
    protected MoneyOrderPojo createOrderDetail(List<CartItemRequest> cartItemRequests, Order order, Boolean isAdminCreate , String langCode) {
        // Log thông tin đầu vào
        log.error("cartItemRequests: {}", cartItemRequests);
        log.error("isAdminCreate: {}", isAdminCreate);
        log.error("order: {}", order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        BigDecimal totalPriceOrderPojo = BigDecimal.ZERO;
        BigDecimal depositAmountOrderPojo = BigDecimal.ZERO;

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
                        moneyItemPojo = getPaymentPriceOnDepositPercentItemAndConditionTerm(cartItemRequest, packages,langCode);
                    }


                    // Lấy thông tin giá trị thanh toán và tiền cọc cho package
                    log.error("check null poiter:{}", moneyItemPojo.getTotalItemPrice());

                    orderDetails.setPackagesId(packages.getId());
                    orderDetails.setTotalPrice(moneyItemPojo.getTotalItemPrice());
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

//                // Cập nhật các giá trị tổng cộng cho tiền cọc và đơn hàng
//                if (moneyItemPojo.getTotalDepositPercent() != null) {
//                    totalDepositPercentOrderPojo += moneyItemPojo.getTotalDepositPercent();
//                }

                depositAmountOrderPojo = depositAmountOrderPojo.add(moneyItemPojo.getTotalItemPrice() .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                totalPriceOrderPojo = totalPriceOrderPojo.add(moneyItemPojo.getTotalItemPrice());
                orderDetailsList.add(orderDetails);
            }

            // Tính toán số tiền cọc và số tiền còn lại cần phải thanh toán

            if(!isAdminCreate){
                moneyOrderPojo.setDepositAmount(depositAmountOrderPojo);
                moneyOrderPojo.setTotalPrice(totalPriceOrderPojo);
            }else {
                moneyOrderPojo.setDepositAmount(null);
                moneyOrderPojo.setTotalPrice(null);
            }


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
    protected MoneyItemPojo getPaymentPriceOnDepositPercentItemAndConditionTerm(CartItemRequest cardItem, Packages packages , String langCode) {
        try {

            I18Language language = i18nLanguageRepository.findByCode(langCode).orElseThrow(()-> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


            int quantiy = cardItem.getQuantity();
            MoneyItemPojo moneyItemPojo = new MoneyItemPojo();


            PaymentTerms paymentTerms = paymentTermsRepository.findByPackagesId(packages.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


            //Condition Monthly Condition Monthly Condition Monthly Condition Monthly Condition Monthly
            if (cardItem.getPaymentConditionOnMonthlyId() != null && cardItem.getPaymentConditionBudgetId() == null) {
                // Nếu kiểu thanh toán là trả theo tháng kiểm tra nó tồn tại không và nó thuộc pakages này không

                PaymentsConditionOnMonthly paymentsConditionOnMonthly = null;



                if(Objects.equals(paymentTerms.getId(), cardItem.getPaymentConditionOnMonthlyId())){
                    paymentsConditionOnMonthly = paymentConditionOnMonthlyRepository.findById(cardItem.getPaymentConditionOnMonthlyId())
                            .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
                }
               // not done not done not done not done not done not done not done not done not done not done not done not done
                Integer packageId = paymentTerms.getPackagesId();
                // kiểm tra packageId từ paymentTerms của PaymentsConditionOnMonthly có
                // khớp với  package hay không
                if (!Objects.equals(packages.getId(), packageId)) {
                    throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND);
                }





                if (paymentTerms.getDepositPercentTranslationKeyId() != null) {

                    //nêú có cọc
                    Integer depositPercent = parseInteger(
                            translationService.getContentFromTranslationByTranslationKeyId(paymentTerms.getDepositPercentTranslationKeyId(), language.getId()),
                            null
                    );
                    BigDecimal priceCondition = paymentsConditionOnMonthly.getPrice();
                    moneyItemPojo.setTotalDepositPercent(quantiy * depositPercent);
                    moneyItemPojo.setTotalItemPrice(priceCondition.multiply(BigDecimal.valueOf(quantiy)));

                } else {
                    // không có cọc
                    BigDecimal priceCondition = paymentsConditionOnMonthly.getPrice();
                    moneyItemPojo.setTotalItemPrice(priceCondition.multiply(BigDecimal.valueOf(quantiy)));
                }

            } else if (cardItem.getPaymentConditionOnMonthlyId() == null && cardItem.getPaymentConditionBudgetId() != null) {
                // logic gì đó
            } else if (paymentTerms.getPriceTranslationKeyId() != null) {


                BigDecimal price = parsePrice(translationService.getContentFromTranslationByTranslationKeyId(
                        paymentTerms.getPriceTranslationKeyId(),
                        language.getId()
                ));

                if (paymentTerms.getDepositPercentTranslationKeyId()!= null) {

                    Integer depositPercent = parseInteger(
                            translationService.getContentFromTranslationByTranslationKeyId(paymentTerms.getDepositPercentTranslationKeyId(), language.getId()),
                            null
                    );

                    moneyItemPojo.setTotalDepositPercent(quantiy * depositPercent);
                    moneyItemPojo.setTotalItemPrice(price.multiply(BigDecimal.valueOf(quantiy)));
                    // không có cọc
                } else {

                    moneyItemPojo.setTotalItemPrice(price.multiply(BigDecimal.valueOf(quantiy)));
                }
                // logic gì đó
            }
            return moneyItemPojo;
        } catch (Exception e) {
            throw e;
        }

    }



    public BigDecimal parsePrice(String priceContent) {
        if (priceContent == null || priceContent.isEmpty()) {
            throw new IllegalArgumentException("Price content is null or empty");
        }
        try {
            return new BigDecimal(priceContent);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + priceContent, e);
        }
    }

    public Integer parseInteger(String content, Integer defaultValue) {
        if (content == null || content.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(content);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Integer format: " + content);
            return defaultValue;
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




}




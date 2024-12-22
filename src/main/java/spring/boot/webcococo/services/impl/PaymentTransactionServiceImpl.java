package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Order;
import spring.boot.webcococo.entities.PaymentTransaction;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.OrderRepository;
import spring.boot.webcococo.repositories.PaymentTransactionalRepository;
import spring.boot.webcococo.services.IPaymentTransactionService;

import java.math.BigDecimal;


@Service
public class PaymentTransactionServiceImpl extends BaseServiceImpl<PaymentTransaction, Integer, PaymentTransactionalRepository> implements IPaymentTransactionService  {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    private final PaymentTransactionalRepository paymentTransactionalRepository;


    private  final OrderRepository orderRepository;

    public PaymentTransactionServiceImpl(PaymentTransactionalRepository paymentTransactionRepository, PaymentTransactionalRepository paymentTransactionalRepository, OrderRepository orderRepository) {
        super(paymentTransactionRepository); // Truyền repository vào lớp cha
        this.paymentTransactionalRepository = paymentTransactionalRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public void createPaymentTransaction(Integer orderId, String transactionCode, BigDecimal amount,String currency, PaymentTransaction.PaymentTransactionStatus status) {


        Order order = orderRepository.findById( orderId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setOrderId(orderId);
        paymentTransaction.setTransactionCode(transactionCode);
        paymentTransaction.setAmount(amount);
        paymentTransaction.setCurrency(currency);
        paymentTransaction.setBankGatewayDetailId(order.getBankGateWayDetailId());
        paymentTransaction.setStatus(status);


    }
}

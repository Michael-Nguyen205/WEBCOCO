package spring.boot.webcococo.services;

import spring.boot.webcococo.entities.PaymentTransaction;

import java.math.BigDecimal;

public interface IPaymentTransactionService {

    void createPaymentTransaction(Integer orderId , String transactionCode, BigDecimal amount  ,String currency, PaymentTransaction.PaymentTransactionStatus status   );
}

package spring.boot.webcococo.services;

import spring.boot.webcococo.models.response.PaymentResponse;

public interface IPaymentService {

    PaymentResponse paymentOnlineOrder(Integer orderId , String ipAdress);


}

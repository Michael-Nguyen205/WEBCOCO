package spring.boot.webcococo.services;

import spring.boot.webcococo.models.requests.PaymentMethodRequest;
import spring.boot.webcococo.models.response.PaymentMethodResponse;

import java.util.List;

public interface IPaymentMethod {


    PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request);
    PaymentMethodResponse getPaymentMethodById(Integer id);
    List<PaymentMethodResponse> getAllPaymentMethod();
}

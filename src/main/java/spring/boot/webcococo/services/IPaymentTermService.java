package spring.boot.webcococo.services;

import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.entities.Products;
import spring.boot.webcococo.models.requests.PaymentTermRequest;
import spring.boot.webcococo.models.response.PaymentTermResponse;

public interface IPaymentTermService {


    PaymentTermResponse createPaymentTerm(PaymentTermRequest request, Integer packdageId,Integer productId);

    PaymentTermResponse updatePaymentTerm(PaymentTermRequest request, Integer packdageId,Integer productId);


    PaymentTermResponse getPaymentTerm(Packages packages, Products products);
    void deletePaymentTerm(Integer id);

}

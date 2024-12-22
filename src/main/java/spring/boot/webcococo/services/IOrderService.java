package spring.boot.webcococo.services;

import spring.boot.webcococo.models.requests.OrderRequest;
import spring.boot.webcococo.models.response.OrderResponse;
import spring.boot.webcococo.models.response.PaymentResponse;

public interface IOrderService {


    OrderResponse createOrder(OrderRequest request , String idAdress);

    String approveOrder(String status ,Integer orderId);

    PaymentResponse payOrder(Integer orderId , String ipAdress,Integer bankGateWayDetalId);
}

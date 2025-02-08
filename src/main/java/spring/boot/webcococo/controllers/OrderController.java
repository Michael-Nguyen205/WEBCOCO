package spring.boot.webcococo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.models.requests.OrderRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.OrderResponse;
import spring.boot.webcococo.models.response.PaymentResponse;
import spring.boot.webcococo.services.impl.OrderServiceImpl;
import spring.boot.webcococo.utils.IpAddressUtil;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order")
@Validated
public class OrderController {


    private final OrderServiceImpl orderService;


    private final IpAddressUtil ipAddressUtil;


    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request , @RequestHeader("Accept-Language") String acceptLanguage) {
        try {

            log.error("đa vao controller");
//            PackagesResponse response = packagesService.createPackage(packagesRequest , packagesRequest.getPaymentTermRequest());
            String ipAdress = ipAddressUtil.getIpAddress(request);
            OrderResponse orderResponse = orderService.createOrder(orderRequest, ipAdress , acceptLanguage);
            return ApiResponse.builder().code(200).detailMess("rgr").result(orderResponse).build();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }





    @PostMapping(value = "/approve/{status}/{orderId}")
    public ApiResponse<?> approveOrder(@PathVariable String status ,@PathVariable Integer orderId,HttpServletRequest request) {
        try {

            log.error("đa vao controller");
            return ApiResponse.builder().code(200).detailMess("rgr").result(orderService.approveOrder(status,orderId )).build();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



//    @PostMapping(value = "/pay/{orderId}/{bankGateWayDetailId}")
//    public ApiResponse<?> payOrder(@PathVariable Integer orderId,@PathVariable Integer bankGateWayDetailId, HttpServletRequest request) {
//        try {
//            String ipAdress = ipAddressUtil.getIpAddress( request);
//            log.error("đa vao controller");
//            log.error("bankGateWayDetailId:{}",bankGateWayDetailId);
//            PaymentResponse paymentResponse = orderService.paymentOnlineOrder (orderId,ipAdress ,bankGateWayDetailId);
//            return ApiResponse.builder().code(200).detailMess("rgr").result(paymentResponse).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }


}

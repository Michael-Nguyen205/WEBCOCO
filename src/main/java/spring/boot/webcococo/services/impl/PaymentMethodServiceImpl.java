package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.BankGatewayDetail;
import spring.boot.webcococo.entities.PaymentMethod;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.PaymentMethodRequest;
import spring.boot.webcococo.models.response.PaymentMethodResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.repositories.BankGatewayDetailRepository;
import spring.boot.webcococo.repositories.PaymentMethodRepository;
import spring.boot.webcococo.services.IPaymentMethod;

import java.util.List;
import java.util.stream.Collectors;

import static spring.boot.webcococo.models.response.PaymentMethodResponse.toPaymentMethodResponse;


@Log4j2
@Service
public class PaymentMethodServiceImpl extends BaseServiceImpl<PaymentMethod, Integer, PaymentMethodRepository> implements IPaymentMethod {

    private final BankGatewayDetailRepository bankGatewayDetailRepository;
    private final BankGateWayRepository bankGateWayRepository;


    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository, BankGatewayDetailRepository bankGatewayDetailRepository, BankGateWayRepository bankGateWayRepository) {
        super(paymentMethodRepository); // Truyền repository vào lớp cha
        this.bankGatewayDetailRepository = bankGatewayDetailRepository;
        this.bankGateWayRepository = bankGateWayRepository;
    }


    @Override
    public PaymentMethodResponse getPaymentMethodById(Integer id) {
        try {
            // Tìm phương thức thanh toán theo ID
            PaymentMethod paymentMethod = findById(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy phương thức thanh toán với id: " + id));

            // Lấy danh sách BankGateway liên quan đến phương thức thanh toán
            List<BankGatewayDetail> bankGatewayDetails = bankGatewayDetailRepository.findAllByPaymentMethodId(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy các cổng ngân hàng cho phương thức thanh toán với id: " + id));

            // Chuyển đổi dữ liệu sang PaymentMethodResponse
            return toPaymentMethodResponse(bankGatewayDetails, paymentMethod, bankGateWayRepository);
        } catch (AppException e) {
            // Xử lý lỗi ứng dụng (AppException)
            log.error("AppException occurred: ", e);
            throw e; // Ném lại ngoại lệ để tiếp tục xử lý ở tầng cao hơn nếu cần
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            log.error("Unexpected error occurred while getting payment method by id", e);
            throw e;
        }
    }


    @Override
    public List<PaymentMethodResponse> getAllPaymentMethod() {
        try {
            // Lấy tất cả phương thức thanh toán
            List<PaymentMethod> paymentMethods = repository.findAll();

            // Chuyển đổi từng PaymentMethod thành PaymentMethodResponse
            return paymentMethods.stream()
                    .map(paymentMethod -> {
                        try {
                            // Lấy danh sách BankGateway liên quan cho mỗi phương thức thanh toán
                            List<BankGatewayDetail> bankGatewayDetails = bankGatewayDetailRepository.findAllByPaymentMethodId(paymentMethod.getId())
                                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy các cổng ngân hàng cho phương thức thanh toán với id: " + paymentMethod.getId()));

                            // Trả về PaymentMethodResponse sau khi chuyển đổi
                            return toPaymentMethodResponse(bankGatewayDetails, paymentMethod, bankGateWayRepository);
                        } catch (AppException e) {
                            // Xử lý lỗi ứng dụng trong map
                            log.error("AppException occurred while processing payment method: ", e);
                            throw e; // Ném lại ngoại lệ để tiếp tục xử lý ở tầng cao hơn nếu cần
                        } catch (Exception e) {
                            // Xử lý các lỗi không mong muốn trong map
                            log.error("Unexpected error occurred while processing payment method", e);
                            throw e;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (AppException e) {
            // Xử lý lỗi ứng dụng (AppException)
            log.error("AppException occurred: ", e);
            throw e; // Ném lại ngoại lệ để tiếp tục xử lý ở tầng cao hơn nếu cần
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            log.error("Unexpected error occurred while getting all payment methods", e);
            throw e;
        }
    }


    @Override
    @Transactional
    public PaymentMethodResponse createPaymentMethod(PaymentMethodRequest request) {


        try {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(request.getName());
            paymentMethod.setDescription(request.getDescription());
            save(paymentMethod);

//            BankGatewayRequest bankGatewayRequest = new BankGatewayRequest();
            List<BankGatewayDetail> bankGatewayDetails = null;

//            if (!request.getBankGatewayRequests().isEmpty()) {
//                for (BankGatewayRequest bankGatewayRequest : request.getBankGatewayRequests()) {
//                    if (bankGatewayRequest.getId() != null) {
//                        BankGateway bankGateway = bankGatewayRepository.findById(bankGatewayRequest.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//                        bankGateways.add(bankGateway);
//                    } else if (bankGatewayRequest.getId() == null) {
//                        BankGateway bankGateway = new BankGateway();
//                        bankGateway.setGatewayUrl(bankGatewayRequest.getGatewayUrl());
//                        bankGateway.setPaymentMethodId(paymentMethod.getId());
//                        assert bankGateways != null;
//                        bankGateways.add(bankGateway);
//                    }
//                }
//
//
//                bankGatewayRepository.saveAll(bankGateways);
//            }

//            assert bankGateways != null;
            return toPaymentMethodResponse(bankGatewayDetails, paymentMethod, bankGateWayRepository);
        } catch (Exception e) {
            throw e;
        }


    }
}
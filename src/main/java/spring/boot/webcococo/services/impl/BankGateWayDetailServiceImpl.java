package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.entities.BankGatewayDetail;
import spring.boot.webcococo.entities.PaymentMethod;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.BankGatewayDetailRequest;
import spring.boot.webcococo.models.response.BankGatewayDetailResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.repositories.BankGatewayDetailRepository;
import spring.boot.webcococo.repositories.PaymentMethodRepository;
import spring.boot.webcococo.services.IBankGateWayDetailService;


@Service
public class BankGateWayDetailServiceImpl extends BaseServiceImpl<BankGatewayDetail, Integer, BankGatewayDetailRepository> implements IBankGateWayDetailService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    private final BankGatewayDetailRepository bankGatewayDetailRepository;

    private final PaymentMethodRepository paymentMethodRepository;
    private final BankGateWayRepository bankGateWayRepository;

    public BankGateWayDetailServiceImpl(BankGatewayDetailRepository bankGatewayDetailRepository, BankGatewayDetailRepository bankGatewayDetailRepository1, PaymentMethodRepository paymentMethodRepository, BankGateWayRepository bankGateWayRepository) {
        super(bankGatewayDetailRepository); // Truyền repository vào lớp cha
        this.bankGatewayDetailRepository = bankGatewayDetailRepository1;
        this.paymentMethodRepository = paymentMethodRepository;
        this.bankGateWayRepository = bankGateWayRepository;
    }


    @Override
    public BankGatewayDetailResponse getBankgateWayDetail(Integer id) {


        BankGatewayDetail bankGatewayDetail = bankGatewayDetailRepository.findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        BankGateWay bankGateWay = bankGateWayRepository.findById(bankGatewayDetail.getBankGatewayId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(bankGatewayDetail.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


        BankGatewayDetailResponse response = BankGatewayDetailResponse.builder()
                .id(bankGatewayDetail.getId())
                .name(bankGateWay.getName())
                .code(bankGatewayDetail.getBankCode())
                .gatewayUrl(bankGatewayDetail.getGatewayUrl())
                .tmlCode(bankGatewayDetail.getTmlCode())
                .secretKey(bankGatewayDetail.getSecretKey())
                .returnUrl(bankGatewayDetail.getReturnUrl())


                .paymentMehodName(paymentMethod.getName())
                .build();
        return response;
    }


    @Override
    public BankGatewayDetailResponse createBankgateWayDetail(Integer paymentMethodId, Integer bankType, BankGatewayDetailRequest bankGatewayDetailRequest) {


        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            BankGateWay bankGateWay = bankGateWayRepository.findById(bankType).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

            BankGatewayDetail bankGatewayDetail = new BankGatewayDetail();
            bankGatewayDetail.setBankCode(bankGatewayDetailRequest.getBankCode());
            bankGatewayDetail.setGatewayUrl(bankGatewayDetailRequest.getGatewayUrl());
            bankGatewayDetail.setPaymentMethodId(paymentMethod.getId());
            bankGatewayDetail.setBankGatewayId(bankGateWay.getId());
            bankGatewayDetailRepository.save(bankGatewayDetail);


            return BankGatewayDetailResponse.builder()
                    .id(bankGatewayDetail.getId())
                    .name(bankGateWay.getName())
                    .code(bankGatewayDetail.getBankCode())
                    .gatewayUrl(bankGatewayDetail.getGatewayUrl())
                    .build();

        } catch (Exception e) {
            throw e;
        }

    }


}

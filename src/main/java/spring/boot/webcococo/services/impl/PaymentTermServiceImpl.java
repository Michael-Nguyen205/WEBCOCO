package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.PaymentConditionOnBudgetRequest;
import spring.boot.webcococo.models.requests.PaymentConditionOnMonthlyRequest;
import spring.boot.webcococo.models.requests.PaymentTermRequest;
import spring.boot.webcococo.models.response.*;
import spring.boot.webcococo.repositories.*;
import spring.boot.webcococo.services.IPaymentTermService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PaymentTermServiceImpl extends BaseServiceImpl<PaymentTerms, Integer, PaymentTermRepository> implements IPaymentTermService {


    private final PaymentMethodPaymentTermrRepository paymentMethodPaymentTermrRepository;
    private final PaymentConditionOnMonthlyRepository paymentConditionOnMonthlyRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentConditionOnBudgetRepository paymentConditionOnBudgetRepository;
    private final PackagesRepository packagesRepository;
    private final ProductRepository productRepository;
    private final BankGatewayDetailRepository bankGatewayDetailRepository;
    private final EntityCascadeDeletionUtil deletionUtil;

    private final PaymentTermRepository paymentTermRepository;

    private  final BankGateWayRepository bankGateWayRepository;


    public PaymentTermServiceImpl(PaymentTermRepository paymentTermRepository, PaymentMethodPaymentTermrRepository paymentMethodPaymentTermrRepository, PaymentConditionOnMonthlyRepository paymentConditionOnMonthlyRepository, PaymentMethodRepository paymentMethodRepository, PaymentConditionOnBudgetRepository paymentConditionOnBudgetRepository, PackagesRepository packagesRepository, ProductRepository productRepository, BankGatewayDetailRepository bankGatewayDetailRepository, EntityCascadeDeletionUtil deletionUtil, PaymentTermRepository paymentTermRepository1, BankGateWayRepository bankGateWayRepository) {
        super(paymentTermRepository); // Truyền repository vào lớp cha
        this.paymentMethodPaymentTermrRepository = paymentMethodPaymentTermrRepository;
        this.paymentConditionOnMonthlyRepository = paymentConditionOnMonthlyRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentConditionOnBudgetRepository = paymentConditionOnBudgetRepository;
        this.packagesRepository = packagesRepository;
        this.productRepository = productRepository;
        this.bankGatewayDetailRepository = bankGatewayDetailRepository;
        this.deletionUtil = deletionUtil;
        this.paymentTermRepository = paymentTermRepository1;
        this.bankGateWayRepository = bankGateWayRepository;
    }


//    @Transactional
//    protected Set<PaymentMethodResponse> createPaymentMethodResponse(Set<Integer> paymentMethodIdList, Integer paymentTermId) {
//        log.error("đã vào createPaymentMethodResponse");
//        try {
//
//            List<PaymentMethod> paymentMethodList = new ArrayList<>();
//            List<PaymentMethodPaymentTerms> paymentMethodPaymentTermsList = new ArrayList<>();
//
//            for (Integer paymentMethodId : paymentMethodIdList) {
//
//                log.error("paymentMethodId:{}", paymentMethodId);
//                log.error("paymentTermId:{}", paymentTermId);
//
//
//                PaymentMethodPaymentTerms paymentMethodPaymentTerms = new PaymentMethodPaymentTerms();
//
//                PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//                paymentMethodPaymentTerms.setPaymentMethodId(paymentMethodId);
//                paymentMethodPaymentTerms.setPaymentTermsId(paymentTermId);
//                paymentMethodPaymentTermsList.add(paymentMethodPaymentTerms);
//                paymentMethodList.add(paymentMethod);
//            }
//            paymentMethodPaymentTermrRepository.saveAll(paymentMethodPaymentTermsList);
//
//
//            // Sử dụng Builder nếu cần
//            Set<PaymentMethodResponse> paymentMethodResponses = new HashSet<>();
//
//            for (PaymentMethod paymentMethod : paymentMethodList) {
//                List<BankGatewayDetail> bankGatewayDetails = bankGatewayDetailRepository.findAllByPaymentMethodId(paymentMethod.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//
//
//
//                PaymentMethodResponse paymentMethodResponse = PaymentMethodResponse.toPaymentMethodResponse(bankGatewayDetails,paymentMethod ,bankGateWayTypeRepository);
//                paymentMethodResponses.add(paymentMethodResponse);
//            }
//            // Thiết lập thời gian tạo và cập nhật
//
//
//            return paymentMethodResponses;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

















    @Transactional
    protected Set<PaymentConditionOnMonthlyResponse> createPaymentConditionOnMonthly(Set<PaymentConditionOnMonthlyRequest> paymentConditionOnMonthlyRequestList, Integer paymentTermId) {

        Set<PaymentsConditionOnMonthly> paymentsConditionOnMonthlies = new HashSet<>();

        for (PaymentConditionOnMonthlyRequest paymentConditionOnMonthlyRequest : paymentConditionOnMonthlyRequestList) {
            PaymentsConditionOnMonthly paymentsConditionOnMonthly = new PaymentsConditionOnMonthly();
            paymentsConditionOnMonthly.setPaymentTermsId(paymentTermId);
            paymentsConditionOnMonthly.setPrice(paymentConditionOnMonthlyRequest.getPrice());
            paymentsConditionOnMonthly.setDurationMonths(paymentConditionOnMonthlyRequest.getDurationMonths());
            paymentsConditionOnMonthlies.add(paymentsConditionOnMonthly);
        }
        try {
            paymentConditionOnMonthlyRepository.saveAll(paymentsConditionOnMonthlies);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return PaymentConditionOnMonthlyResponse.toPaymentConditionOnMonthlyResponse(paymentsConditionOnMonthlies);


    }

    @Transactional
    protected Set<PaymentConditionOnBudgetResponse> createPaymentConditionOnBudget(Set<PaymentConditionOnBudgetRequest> paymentConditionOnBudgetRequestList, Integer paymentTermId) {

        Set<PaymentsConditionOnBudget> paymentsConditionOnBudgetList = new HashSet<>();

        for (PaymentConditionOnBudgetRequest paymentConditionOnBudgetRequest : paymentConditionOnBudgetRequestList) {
            PaymentsConditionOnBudget paymentsConditionOnBudget = new PaymentsConditionOnBudget();
            paymentsConditionOnBudget.setMaxBudget(paymentConditionOnBudgetRequest.getMaxBudget());
            paymentsConditionOnBudget.setMinBudget(paymentConditionOnBudgetRequest.getMinBudget());
            paymentsConditionOnBudget.setPaymentTermsId(paymentTermId);

            if (paymentConditionOnBudgetRequest.getFixedFee() != null && paymentConditionOnBudgetRequest.getPercentageFee() == null) {
                paymentsConditionOnBudget.setFixedFee(paymentConditionOnBudgetRequest.getFixedFee());

            } else if (paymentConditionOnBudgetRequest.getFixedFee() == null && paymentConditionOnBudgetRequest.getPercentageFee() != null) {
                paymentsConditionOnBudget.setPercentageFee(paymentConditionOnBudgetRequest.getPercentageFee());
            }
            paymentsConditionOnBudgetList.add(paymentsConditionOnBudget);
        }

        try {
            paymentConditionOnBudgetRepository.saveAll(paymentsConditionOnBudgetList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return PaymentConditionOnBudgetResponse.toPaymentConditionOnBudgetResponse(paymentsConditionOnBudgetList);
    }








    @Transactional
    protected Set<PaymentConditionOnMonthlyResponse> updatePaymentConditionOnMonthly(Set<PaymentConditionOnMonthlyRequest> paymentConditionOnMonthlyRequestList, Integer paymentTermId) {

        List<PaymentsConditionOnMonthly> paymentsConditionOnMonthlies = paymentConditionOnMonthlyRepository.findAllByPaymentTermsId(paymentTermId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        paymentConditionOnMonthlyRepository.deleteAll(paymentsConditionOnMonthlies);

        for (PaymentConditionOnMonthlyRequest paymentConditionOnMonthlyRequest : paymentConditionOnMonthlyRequestList) {
            PaymentsConditionOnMonthly paymentsConditionOnMonthly = new PaymentsConditionOnMonthly();
            paymentsConditionOnMonthly.setPaymentTermsId(paymentTermId);
            paymentsConditionOnMonthly.setPrice(paymentConditionOnMonthlyRequest.getPrice());
            paymentsConditionOnMonthly.setDurationMonths(paymentConditionOnMonthlyRequest.getDurationMonths());
            paymentsConditionOnMonthlies.add(paymentsConditionOnMonthly);
        }
        try {
            paymentConditionOnMonthlyRepository.saveAll(paymentsConditionOnMonthlies);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return PaymentConditionOnMonthlyResponse.toPaymentConditionOnMonthlyResponse(paymentsConditionOnMonthlies.stream().collect(Collectors.toSet()));
    }




//    @Transactional
//    public PaymentTermResponse toPaymentTermResponse(PaymentTerms paymentTerms, List<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponseList, List<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses, List<PaymentMethodResponse> paymentMethodResponses) {
//        PaymentTermResponse response = new PaymentTermResponse();
//        response.setName(paymentTerms.getName());
//        response.setPrice(paymentTerms.getPrice());
////        response.setPackagesId();
////        response.setProductId();
//
//        log.error("paymentConditionOnMonthlyResponses", paymentConditionOnMonthlyResponses);
//        response.setPaymentConditionOnBudgetResponses(paymentConditionOnBudgetResponseList);
//        response.setPaymentConditionOnMonthlyResponses(paymentConditionOnMonthlyResponses);
//        response.setPaymentMethodResponses(paymentMethodResponses);
//        return response;
//    }


    @Transactional
    @Override
    public PaymentTermResponse createPaymentTerm(PaymentTermRequest request, Integer packageId, Integer productId) {
        log.error("payment_condition_on_monthly ", request.getPaymentConditionOnMonthlyRequestList());


        PaymentTerms paymentTerms = new PaymentTerms();
        paymentTerms.setName(request.getName());

        try {
            if (request.getPrice() != null) {
                paymentTerms.setPrice(request.getPrice());
            }


            if (packageId != null && productId == null) {
                paymentTerms.setPackagesId(packageId);
            } else if (productId == null && packageId != null) {
                paymentTerms.setProductId(productId);
            }


            if (request.getStartAt() != null) {
                paymentTerms.setStartAt(request.getStartAt());
                paymentTerms.setEndAt(request.getEndAt());
            }
            save(paymentTerms);
        } catch (Exception e) {
            throw e;
        }


        Set<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponseList = new HashSet<>();
        Set<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses = new HashSet<>();
        if (request.getPaymentConditionOnBudgetRequestList() != null) {
            paymentConditionOnBudgetResponseList = createPaymentConditionOnBudget(request.getPaymentConditionOnBudgetRequestList(), paymentTerms.getId());
        }

        if (request.getPaymentConditionOnMonthlyRequestList() != null) {

            paymentConditionOnMonthlyResponses = createPaymentConditionOnMonthly(request.getPaymentConditionOnMonthlyRequestList(), paymentTerms.getId());
        }



//        return toPaymentTermResponse(paymentTerms, paymentConditionOnBudgetResponseList, paymentConditionOnMonthlyResponses, paymentMethodResponses);
        return PaymentTermResponse.toPayMentTermResponse(paymentTerms, paymentConditionOnBudgetResponseList, paymentConditionOnMonthlyResponses);
    }



    @Override
    public PaymentTermResponse getPaymentTerm(Packages packages, Products products) {
        return null;
    }


//    @Override
//    public PaymentTermResponse getPaymentTerm(Packages packages, Products products) {
//
//        try {
//            PaymentTerms paymentTerms = new PaymentTerms();
//            List<PaymentMethodResponse> paymentMethodResponses = new ArrayList<>();
//            if (packages != null && products == null) {
//                paymentTerms = paymentTermRepository.findByPackagesId(packages.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//                PaymentMethodPaymentTerms paymentMethodPaymentTerms = paymentMethodPaymentTermrRepository.
//                PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethod
//            }
////            return   PaymentTermResponse.toPayMentTermResponse(paymentTerms, paymentConditionOnBudgetResponseList, paymentConditionOnMonthlyResponses, paymentMethodResponses);
//            return null;
//        } catch (Exception e) {
//            throw e;
//        }
//    }

















    @Transactional
    @Override
    public PaymentTermResponse updatePaymentTerm(PaymentTermRequest request, Integer packageId, Integer productId) {
        log.error("payment_condition_on_monthly ", request.getPaymentConditionOnMonthlyRequestList());


        PaymentTerms paymentTerms = paymentTermRepository.findByPackagesId(packageId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        paymentTerms.setName(request.getName());


        try {
            if (request.getPrice() != null) {
                paymentTerms.setPrice(request.getPrice());
            }


            if (packageId != null && productId == null) {
                paymentTerms.setPackagesId(packageId);
            } else if (productId == null && packageId != null) {
                paymentTerms.setProductId(productId);
            }


            if (request.getStartAt() != null) {
                paymentTerms.setStartAt(request.getStartAt());
                paymentTerms.setEndAt(request.getEndAt());
            }
            save(paymentTerms);
        } catch (Exception e) {
            throw e;
        }


        Set<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponses = new HashSet<>();
        Set<PaymentConditionOnMonthlyResponse> paymentConditionOnMonthlyResponses = new HashSet<>();


        if (request.getPaymentConditionOnBudgetRequestList() != null) {
//            paymentConditionOnBudgetResponses = createPaymentConditionOnBudget(request.getPaymentConditionOnBudgetRequestList(), paymentTerms.getId());
        }

        if (request.getPaymentConditionOnMonthlyRequestList() != null) {
            paymentConditionOnMonthlyResponses = updatePaymentConditionOnMonthly(request.getPaymentConditionOnMonthlyRequestList(), paymentTerms.getId());
        }



//        return toPaymentTermResponse(paymentTerms, paymentConditionOnBudgetResponseList, paymentConditionOnMonthlyResponses, paymentMethodResponses);
        return PaymentTermResponse.toPayMentTermResponse(paymentTerms, paymentConditionOnBudgetResponses, paymentConditionOnMonthlyResponses);
    }














    @Override
    @Transactional
    public void deletePaymentTerm(Integer id) {

        try {
            deleteById(id);
            deletionUtil.deleteByParentId(id, "payment_terms_id");
        } catch (Exception e) {
            throw e;
        }
    }


}
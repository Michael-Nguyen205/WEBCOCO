package spring.boot.webcococo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.PaymentMethod;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.PaymentMethodRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.PaymentMethodResponse;
import spring.boot.webcococo.repositories.PaymentMethodRepository;
import spring.boot.webcococo.services.impl.PaymentMethodServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payment_method")
public class PaymentMethodController {

    private final ModelMapper modelMapper;
    private final PaymentMethodServiceImpl  paymentMethodService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final PaymentMethodRepository paymentMethodRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createPaymentMethod(@RequestBody PaymentMethodRequest request) {
        // Thiết lập thông tin đơn hàng
        log.error("vaoooooooocooooo");

        try {
            PaymentMethodResponse response = paymentMethodService.createPaymentMethod(request); // Lưu model vào cơ sở dữ liệu
    return ResponseEntity.ok(new ApiResponse(null, "Model updated successfully", null, response));

        } catch (Exception e) {
            log.error("Error creating model", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Error creating model", null, null));
        }
    }






    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentMethodById(@PathVariable Integer id) {
        log.info("Fetching payment method with id: {}", id);
        try {
            PaymentMethodResponse response = paymentMethodService.getPaymentMethodById(id);
            return ResponseEntity.ok(new ApiResponse(null, "Payment method fetched successfully", null, response));
        } catch (AppException e) {
            log.error("Error occurred while fetching payment method", e);
            throw e;
        } catch (Exception e) {
           throw e;
        }
    }


    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllPaymentMethods() {
        log.info("Fetching all payment methods");
        try {
            List<PaymentMethodResponse> responses = paymentMethodService.getAllPaymentMethod();

            return ResponseEntity.ok(new ApiResponse(null, "Payment methods fetched successfully", null, responses));
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all payment methods", e);
            throw e;        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePaymentMedthod(@PathVariable Integer id) {
        log.error("đã vào trong deletemodels");
         try {
             PaymentMethod paymentMethod = paymentMethodService.findById(id)
                     .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));

             paymentMethod.setIdActive(false);


             return ResponseEntity.ok(new ApiResponse(null, "Model deleted successfully", null, null));


         } catch (AppException e) {
             // Nếu có lỗi của ứng dụng, trả về mã lỗi 400 hoặc 500 và thông báo lỗi
             log.error("Error occurred while deleting model", e);
             throw e; // Ném lại ngoại lệ AppException
         } catch (Exception e) {
             // Bắt tất cả các lỗi khác và trả về mã lỗi 500
             log.error("Unexpected error occurred", e);
             throw e; // Ném lại ngoại lệ chung
         }
    }









//    @GetMapping(value = "/{id}")
//    public ResponseEntity<ModelResponse> getModel(@PathVariable Integer id) {
//        log.error("Đã vào đây actions controller");
//        return modelsRepository.findById(id)
//                .map(entity -> ResponseEntity.ok(ModelResponse.builder()
//                        .modelName(entity.getName())
//                        .modelId(entity.getId())
//                        .modelDescription(entity.getDescription())
//                        .build()))
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(null));
//    }
//
//    @GetMapping(value = "")
//    public ResponseEntity<List<ModelResponse>> getAllModels() {
//        log.error("Đã vào đây models controller");
//
//        try {
//            // Gọi service để lấy danh sách các model
//            List<ModelResponse> modelResponses = modelService.findAll()
//                    .stream()
//                    .map(model -> ModelResponse.builder()
//                            .modelId(model.getId())
//                            .modelName(model.getName())
//                            .build())
//                    .toList();
//
//            return ResponseEntity.ok(modelResponses); // Trả về kết quả thành công
//
//        } catch (Exception e) {
//            // Log lỗi và trả về phản hồi lỗi
//            log.error("Lỗi khi lấy danh sách models: {}", e.getMessage(), e);
//
//            // Trả về mã lỗi và thông báo lỗi cho client
//        throw e;
//        }
//    }


//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse> updateModel(@RequestBody CreateModelRequest request, @PathVariable Integer id) {
//        Optional<Models> existingEntityOptional = modelsRepository.findById(id);
//
//        if (existingEntityOptional.isPresent()) {
//            Models existingEntity = existingEntityOptional.get();
//            existingEntity.setName(request.getName());
//            existingEntity.setDescription(request.getDescription());
//
//            try {
//                Models updatedEntity = modelService.update(existingEntity);
//                return ResponseEntity.ok(new ApiResponse(null, "Model updated successfully", null, updatedEntity));
//            } catch (Exception e) {
//                log.error("Error updating model", e);
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(new ApiResponse(null, "Error updating model", null, null));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse(null, "Model not found", null, null));
//        }
//    }
//



}
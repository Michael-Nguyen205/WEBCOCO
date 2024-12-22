package spring.boot.webcococo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.BankGatewayDetailRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.BankGatewayDetailResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.services.impl.BankGateWayDetailServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bank_gateway_detail")
public class BankGateWayDetailController {

    private final BankGateWayDetailServiceImpl bankGateWayDetailDetailService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final BankGateWayRepository bankGateWayRepository;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBankGateWayDetail(@PathVariable Integer id) {

        try {
            log.error("Đã vào đây actions controller");
            BankGatewayDetailResponse response = bankGateWayDetailDetailService.getBankgateWayDetail(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw e;
        }

    }


    @PostMapping("/{paymentMethodId}/{bankType}")
    public ResponseEntity<?> createBankGateWayDetail(
            @PathVariable Integer paymentMethodId,
            @PathVariable Integer bankType,
            @RequestBody BankGatewayDetailRequest bankGatewayDetailRequest) {
        try {
            // Gọi service để tạo BankGateway
            BankGatewayDetailResponse response = bankGateWayDetailDetailService.createBankgateWayDetail(paymentMethodId, bankType, bankGatewayDetailRequest);
            return ResponseEntity.ok(new ApiResponse(null, "Action created successfully", null, response));
        } catch (AppException e) {
            // Xử lý lỗi cụ thể từ ứng dụng
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi hệ thống không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }


//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse updateBankGateWayType(@RequestBody @Valid CreateActionRequest request, @PathVariable Integer id) {
//        BankGateWayType bankGateWayTypeExisting = bankGateWayTypeRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
//        bankGateWayTypeExisting.setName(request.getName());
//        BankGateWayType updatedEntity = bankGateWayTypeService.update(bankGateWayTypeExisting);
//        return new ApiResponse(null, "Action updated successfully", null, updatedEntity);
//    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteBankGateWayDetail(@PathVariable Integer id) {
        try {
            log.error("Đã vào trong delete actions");
            // Truyền enum vào phương thức cascadeDeletion
            bankGateWayDetailDetailService.deleteById(id);
            // Chạy cascade deletion để xóa các bảng liên quan
            deletionUtil.deleteByParentId(id, "bank_gateway_id");

            return new ApiResponse(null, "Action deleted successfully", null, null);
        } catch (Exception e) {
            throw e;
        }

    }
}

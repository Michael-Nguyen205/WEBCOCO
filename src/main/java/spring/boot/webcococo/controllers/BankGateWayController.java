package spring.boot.webcococo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.BankGateWayRequest;
import spring.boot.webcococo.models.requests.CreateActionRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.services.impl.BankGateWayServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bank_gateway")
public class BankGateWayController {

    private final BankGateWayServiceImpl bankGateWayService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final BankGateWayRepository bankGateWayRepository;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBankGateWay(@PathVariable Integer id) {
        try {
            log.error("Đã vào đây actions controller");
            BankGateWay bankGateWay = bankGateWayService.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
            return ResponseEntity.ok(bankGateWay);
        } catch (AppException e) {
            log.error("Lỗi: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getBankGateWay() {
        try {
            log.error("Đã vào đây actions controller");
            List<BankGateWay> bankGateWay = bankGateWayService.findAll();
            return ResponseEntity.ok(bankGateWay);
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createBankGateWay(@RequestBody BankGateWayRequest request) {
        try {
            BankGateWay bankGateWay = new BankGateWay();
            bankGateWay.setName(request.getName());
            bankGateWay.setIdActive(true);
            BankGateWay savedBankGateWay = bankGateWayService.save(bankGateWay);
            return ResponseEntity.ok(new ApiResponse(null, "Action created successfully", null, savedBankGateWay));
        } catch (AppException e) {
            log.error("Lỗi: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateBankGateWay(@RequestBody @Valid CreateActionRequest request, @PathVariable Integer id) {
        try {
            BankGateWay bankGateWayExisting = bankGateWayRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
            bankGateWayExisting.setName(request.getName());
            BankGateWay updatedEntity = bankGateWayService.update(bankGateWayExisting);
            return ResponseEntity.ok(new ApiResponse(null, "Action updated successfully", null, updatedEntity));
        } catch (AppException e) {
            log.error("Lỗi: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBankGateWay(@PathVariable Integer id) {
        try {
            log.error("Đã vào trong delete actions");
            bankGateWayService.deleteById(id);
            deletionUtil.deleteByParentId(id, "bank_gateway_type_id");
            return ResponseEntity.ok(new ApiResponse(null, "Action deleted successfully", null, null));
        } catch (AppException e) {
            log.error("Lỗi: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}

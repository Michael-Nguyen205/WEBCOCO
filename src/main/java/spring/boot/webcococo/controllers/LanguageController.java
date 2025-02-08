package spring.boot.webcococo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.BankGateWayRequest;
import spring.boot.webcococo.models.requests.CreateActionRequest;
import spring.boot.webcococo.models.requests.I18LanguageRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.repositories.I18nLanguageRepository;
import spring.boot.webcococo.services.impl.BankGateWayServiceImpl;
import spring.boot.webcococo.services.impl.I18nLanguageServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/language")
public class LanguageController {

    private final I18nLanguageServiceImpl i18nLanguageService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final I18nLanguageRepository i18nLanguageRepository;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getLanguageById(@PathVariable Integer id , HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            log.error("Đã vào đây actions controller");
            log.error("ipAddress:{}",ipAddress);

            I18Language I18Language = i18nLanguageService.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
            return ResponseEntity.ok(I18Language);
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
    public ResponseEntity<?> getAllLanguage() {
        try {
            log.error("Đã vào đây actions controller");
            List<I18Language> i18Languages = i18nLanguageService.findAll();
            return ResponseEntity.ok(i18Languages);
        } catch (Exception e) {
            log.error("Lỗi hệ thống: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createLanguage(@RequestBody I18LanguageRequest request  ) {
        try {
            I18Language i18Language = new I18Language();
            i18Language.setCode(request.getCode());
            i18Language.setLocale(request.getLocale());
            i18Language.setName(request.getName());
            i18Language.setIsActive(true);
            i18Language.setNativeName(request.getNativeName());
            i18Language = i18nLanguageService.save(i18Language);
            return ResponseEntity.ok(new ApiResponse(null, "Action created successfully", null, i18Language));
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
    public ResponseEntity<?> updateLanguage(@RequestBody @Valid I18LanguageRequest request, @PathVariable Integer id) {
        try {
            I18Language i18Language = i18nLanguageService.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy I18Language với id: " + id));

            i18Language.setCode(request.getCode());
            i18Language.setLocale(request.getLocale());
            i18Language.setName(request.getName());
            i18Language.setIsActive(request.getIsActive());
            i18Language.setNativeName(request.getNativeName());
            i18Language = i18nLanguageService.update(i18Language);
            return ResponseEntity.ok(new ApiResponse(null, "Action updated successfully", null, i18Language));
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
    public ResponseEntity<?> deleteLanguage(@PathVariable Integer id) {
        try {
            log.error("Đã vào trong delete actions");
            i18nLanguageService.deleteById(id);
            deletionUtil.deleteByParentId(id, "i18n_language_id");
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

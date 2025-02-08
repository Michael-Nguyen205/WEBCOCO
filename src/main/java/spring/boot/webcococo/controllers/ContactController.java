package spring.boot.webcococo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.Contact;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.BankGatewayDetailRequest;
import spring.boot.webcococo.models.requests.ContactRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.BankGatewayDetailResponse;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.repositories.ContactRepository;
import spring.boot.webcococo.services.impl.BankGateWayDetailServiceImpl;
import spring.boot.webcococo.services.impl.ContactServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/contact")
public class ContactController {

    private final ContactServiceImpl contactService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ContactRepository contactRepository;





    @PostMapping("")
    public ResponseEntity<?> createContact(
            @RequestHeader("Accept-Language") String acceptLanguage,
            @RequestBody ContactRequest request) {
        try {
            // Gọi service để tạo BankGateway
            Contact contact = contactService.createContact(request,acceptLanguage);
            return ResponseEntity.ok(new ApiResponse(null, "Action created successfully", null, contact));
        } catch (AppException e) {
            // Xử lý lỗi cụ thể từ ứng dụng
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi hệ thống không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }



//    @PostMapping("")
//    public ResponseEntity<?> createContact(
//            @RequestBody ContactRequest request) {
//        try {
//            // Gọi service để tạo BankGateway
//            return ResponseEntity.ok("duoc roi");
//        } catch (AppException e) {
//            // Xử lý lỗi cụ thể từ ứng dụng
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            // Xử lý lỗi hệ thống không mong muốn
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
//        }
//    }



    @GetMapping("")
    public ResponseEntity<?> getAllContact(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String locale,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
            ) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page, limit,
                    //Sort.by("createdAt").descending()
                    Sort.by("id").ascending()
            );
            // Gọi service để tạo BankGateway
            List<Contact> contact = contactService.getAllContact(keyword,locale,status,pageRequest);

            return ResponseEntity.ok(new ApiResponse(null, "Action created successfully", null, contact));
        } catch (AppException e) {
            // Xử lý lỗi cụ thể từ ứng dụng
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi hệ thống không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }






}

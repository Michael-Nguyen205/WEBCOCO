package spring.boot.webcococo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.models.requests.TranslationRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.services.impl.TranslationServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/translation")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationServiceImpl translationService;


    @PutMapping("/{languagId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> upDateTranslation(@PathVariable Integer languagId, @RequestBody List<TranslationRequest> translationRequests) {
        try {

            // Đúng cú pháp:
            return ResponseEntity.ok(new ApiResponse(200, "Success", null, translationService.upDateTranslation(translationRequests, languagId)));
//            return ResponseEntity.ok("ss");

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }






    @PostMapping("/{languagId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createTranslation(@PathVariable Integer languagId, @RequestBody List<TranslationRequest> translationRequests) {
        try {

            // Đúng cú pháp:
            return ResponseEntity.ok(new ApiResponse(200, "Success", null, translationService.createTranslation(translationRequests, languagId)));
//            return ResponseEntity.ok("ss");

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }


}

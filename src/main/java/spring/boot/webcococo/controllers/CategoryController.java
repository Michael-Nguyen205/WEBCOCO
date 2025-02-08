package spring.boot.webcococo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.entities.ProductPackageImages;
import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.CategoriesResponse;
import spring.boot.webcococo.models.response.CategoryTranslationsResponse;
import spring.boot.webcococo.services.impl.CategoriesServiceImpl;
import spring.boot.webcococo.services.impl.CategoriesTranslaionServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;
import spring.boot.webcococo.utils.MessageKeys;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
private final CategoriesServiceImpl categoriesService;
private  final CategoriesTranslaionServiceImpl categoriesTranslaionService;
    private final EntityCascadeDeletionUtil deletionUtil;


    @PostMapping("/{languageId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createCategoryTree(@RequestBody CategoriesRequest request,@PathVariable Integer languageId) {
        try {
            log.error("da vao request la: {}", request);
            CategoriesResponse response = categoriesService.saveCategoryTree(request,languageId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
        throw e;
        }
    }





    @PutMapping ("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCategoryTree(@RequestBody CategoriesRequest request,@PathVariable Integer id) {
        try {
            log.error("da vao request la: {}", request);
            CategoriesResponse response = categoriesService.updateCategoryTree(request,id);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }





    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCategoryTree(@PathVariable Integer id,@RequestHeader("Accept-Language") String acceptLanguage) {
        try {
            log.error("da vao id la: {}", id);
            CategoriesResponse response = categoriesService.getCategoryTree(id,acceptLanguage);

            // Đúng cú pháp:
            return ResponseEntity.ok(new ApiResponse(200, "Success", null, response));
//            return ResponseEntity.ok("ss");

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }




    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllCategoryTree(@RequestHeader("Accept-Language") String acceptLanguage) {
        try {
            log.error("da vao ");
            List<CategoriesResponse> response = categoriesService.getAllCategoryTree(acceptLanguage);
            log.error("lisstresponse:{}",response);
            // Đúng cú pháp:
            return ResponseEntity.ok(new ApiResponse(200, "Success", null, response));
//            return ResponseEntity.ok("ss");

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }



    @DeleteMapping  ("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCategoryTree(@PathVariable Integer id) {
        try {
           categoriesService.deleteCategoryTree(id);
            return ResponseEntity.ok(new ApiResponse(200, "Success", "xoa thanh ccoong", null));

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }





    @PostMapping(value = "uploads/package/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //POST http://localhost:8088/v1/api/products
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Integer categories,
            @ModelAttribute("files") MultipartFile file
    ) {
        try {
            String imageName = categoriesService.createCategoriesImages(categories,file);
            return ResponseEntity.ok().body(imageName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/translations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllCategoryTreeWithTranslationsList() {
        try {
            log.error("da vao ");
            List<CategoryTranslationsResponse> response = categoriesTranslaionService.getAllCategoriesAndTranslationFiels();
            log.error("lisstresponse:{}",response);
            // Đúng cú pháp:
            return ResponseEntity.ok(new ApiResponse(200, "Success", null, response));
//            return ResponseEntity.ok("ss");

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }





}

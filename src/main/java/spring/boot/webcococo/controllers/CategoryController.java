package spring.boot.webcococo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.CategoriesResponse;
import spring.boot.webcococo.services.impl.CategoriesServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
private final CategoriesServiceImpl categoriesService;
    private final EntityCascadeDeletionUtil deletionUtil;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> saveCategoryTree(@RequestBody CategoriesRequest request) {
        try {
            log.error("da vao request la: {}", request);
            CategoriesResponse response = categoriesService.saveCategoryTree(request);
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
    public ResponseEntity<?> getCategoryTree(@PathVariable Integer id) {
        try {
            log.error("da vao id la: {}", id);
            CategoriesResponse response = categoriesService.getCategoryTree(id);

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
    public ResponseEntity<?> getAllCategoryTree() {
        try {
            log.error("da vao ");
            List<CategoriesResponse> response = categoriesService.getAllCategoryTree();
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
    public ResponseEntity<?> updateCategoryTree(@PathVariable Integer id) {
        try {
           categoriesService.deleteCategoryTree(id);
            return ResponseEntity.ok(new ApiResponse(200, "Success", "xoa thanh ccoong", null));

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }





}

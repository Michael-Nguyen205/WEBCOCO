package spring.boot.webcococo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.webcococo.models.requests.ProductRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.ProductResponse;
import spring.boot.webcococo.services.impl.ProductsServiceIpml;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductsServiceIpml productsServiceIpml;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest request) {
        // Thiết lập thông tin sản phẩm

        try {
            ProductResponse response = productsServiceIpml.createProduct(request); // Lưu model vào cơ sở dữ liệu
            if (response != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse(null, "Model created successfully", null, response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, "Failed to create model: no response", null, null));
            }
        } catch (Exception e) {
            log.error("Error creating model", e);
            throw e;
        }
    }


}

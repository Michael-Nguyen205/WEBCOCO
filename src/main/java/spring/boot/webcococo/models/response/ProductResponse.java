package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.ProductPackageImages;
import spring.boot.webcococo.entities.Products;
import spring.boot.webcococo.entities.ProductsFeature;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("categories_id")
    private Integer categoriesId;

    @JsonProperty("package_id")
    private Integer packageId;

    @JsonProperty("main_images")
    private String mainImages;

    @JsonProperty("base_price")
    private Integer basePrice;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("product_feature_response_list")
    private List<ProductFeatureResponse> productFeatureResponseList;

    @JsonProperty("product_images")
    private List<ProductPackageImages> productImages = new ArrayList<>();

    // Phương thức chuyển đổi Products thành ProductResponse
    public static ProductResponse toProductResponse(Products product, List<ProductsFeature> productsFeatures) {

        List<ProductFeatureResponse> productFeatureResponseList = ProductFeatureResponse.toProductFeatureResponse(productsFeatures);

        // Sử dụng Builder nếu cần
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .basePrice(product.getBasePrice())
                .packageId(product.getPackageId())
                .categoriesId(product.getCategoriesId())
                .productFeatureResponseList(productFeatureResponseList)
                .build();

        // Thiết lập thời gian tạo và cập nhật nếu cần
        return productResponse;
    }
}

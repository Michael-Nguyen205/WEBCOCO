package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.ProductsFeature;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProductFeatureResponse {

    private Integer id;
    private String name;
    private String description;

    // Phương thức chuyển đổi Products thành ProductResponse
    public static  List<ProductFeatureResponse> toProductFeatureResponse(List<ProductsFeature> productsFeatures) {
        // Sử dụng Builder nếu cần

        List<ProductFeatureResponse> productFeatureResponseList = new ArrayList<>();

        for (ProductsFeature productsFeature : productsFeatures ){

            ProductFeatureResponse productFeatureResponse = ProductFeatureResponse.builder()
                    .id(productsFeature.getId())
                    .name(productsFeature.getName())
                    .description(productsFeature.getDescription())
                    .build();

            productFeatureResponseList.add(productFeatureResponse);
        }



        // Thiết lập thời gian tạo và cập nhật


        return productFeatureResponseList;
    }





}

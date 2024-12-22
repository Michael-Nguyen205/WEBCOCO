package spring.boot.webcococo.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull(message = "Categories ID must not be null")
    private Integer categoriesId;

    private Integer packageId;

    private Integer basePrice;

    @NotNull(message = "Product name must not be null")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private List<ProductFeatureRequest> featureRequestList;


}

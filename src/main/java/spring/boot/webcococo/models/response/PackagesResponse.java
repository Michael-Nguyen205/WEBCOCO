package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.entities.PackagesFeature;
import spring.boot.webcococo.entities.ProductPackageImages;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackagesResponse {

    private Integer id;

    private String name;

    private String description;

    @JsonProperty("categories_id")
    private Integer categoriesId;


    @JsonProperty("packages_images")
    private Set<PackageImagesResponse> packageImagesResponses;

    @JsonProperty("packages_feature")
    private Set<PackagesFeatureResponse> packagesFeatureResponses;

//    @JsonProperty("payment_terms")
//    private Set<PaymentTermResponse> paymentTerms;

    @JsonProperty("payment_term")
    private PaymentTermResponse paymentTermResponse;


    private Integer deposit;

    public static PackagesResponse toPackagesResponse(Packages packages, Set<PackagesFeature> packagesFeatureList, PaymentTermResponse paymentTermResponse, Set<ProductPackageImages> packageImages ) {

        return PackagesResponse.builder()
                .id(packages.getId())
                .name(packages.getName())
                .description(packages.getDescription())
                .categoriesId(packages.getCategoriesId())
//                .packageImagesResponses(PackageImagesResponse.   )
                .packagesFeatureResponses(PackagesFeatureResponse.toPackagesResponseList(packagesFeatureList))
                .paymentTermResponse(paymentTermResponse)
                .build();
    }

}

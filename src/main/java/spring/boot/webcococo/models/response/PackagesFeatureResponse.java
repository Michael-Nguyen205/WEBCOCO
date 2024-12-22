package spring.boot.webcococo.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.PackagesFeature;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PackagesFeatureResponse {
    private  Integer id;

    private String feature;

    private Integer packageId;




    public static Set<PackagesFeatureResponse> toPackagesResponseList(Set<PackagesFeature> packagesFeatureList){
        Set<PackagesFeatureResponse> packagesFeatureResponses = new HashSet<>();


        for (PackagesFeature packagesFeature : packagesFeatureList ){
            PackagesFeatureResponse productFeatureResponse = PackagesFeatureResponse.builder()
                    .id(packagesFeature.getId())
                    .feature(packagesFeature.getFeature())
                    .packageId(packagesFeature.getPackageId() )
                    .build();
            packagesFeatureResponses.add(productFeatureResponse);
        }

        return  packagesFeatureResponses;
    }


}

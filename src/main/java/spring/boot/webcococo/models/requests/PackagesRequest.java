package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PackagesRequest  {



    private String name;

    private String description;

    @JsonProperty("categories_id")
    private Integer categoriesId;

    @JsonProperty("packages_feature_request_list")
    private List<PackageFeatureRequest> packagesFeatureRequestList;

    @JsonProperty("payment_term")
    private PaymentTermRequest paymentTermRequest;

    @JsonProperty("deposit_Percent")
    private Integer depositPercent;


}

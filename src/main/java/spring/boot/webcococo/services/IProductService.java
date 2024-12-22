package spring.boot.webcococo.services;

import spring.boot.webcococo.models.requests.ProductRequest;
import spring.boot.webcococo.models.response.ProductResponse;

public interface IProductService {

    ProductResponse createProduct(ProductRequest request);

}

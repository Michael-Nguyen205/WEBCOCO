package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.entities.Products;
import spring.boot.webcococo.entities.ProductsFeature;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.ProductFeatureRequest;
import spring.boot.webcococo.models.requests.ProductRequest;
import spring.boot.webcococo.models.response.ProductResponse;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.repositories.PackagesRepository;
import spring.boot.webcococo.repositories.ProductFeatureRepository;
import spring.boot.webcococo.repositories.ProductRepository;
import spring.boot.webcococo.services.IProductService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsServiceIpml extends BaseServiceImpl<Products, Integer, ProductRepository> implements IProductService {
    private final CategoryRepository categoryRepository;
    private final PackagesRepository packagesRepository;

    private final ProductFeatureRepository productFeatureRepository;


    public ProductsServiceIpml(ProductRepository repository, CategoryRepository categoryRepository, PackagesRepository packagesRepository, ProductFeatureRepository productFeatureRepository) {
        super(repository); // Truyền repository vào lớp cha
        this.categoryRepository = categoryRepository;
        this.packagesRepository = packagesRepository;
        this.productFeatureRepository = productFeatureRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        // 1. Khởi tạo đối tượng Product từ ProductRequest
        Products products = new Products();

        try{

            products.setName(request.getName());
            products.setBasePrice(request.getBasePrice() != null ? request.getBasePrice() : null);
            products.setDescription(request.getDescription() != null ? request.getDescription() : null);
            setCategoryAndPackageForProduct(request, products);
            products = save(products);
        }catch (Exception e){
            throw e;
        }

        List<ProductsFeature> productsFeatureList = saveProductFeature(request.getFeatureRequestList(),products);



        // 4. Tạo đối tượng ProductResponse từ Products đã lưu
        ProductResponse response =  ProductResponse.toProductResponse(products,productsFeatureList);

        return response;
    }

    // Phương thức khởi tạo Products từ ProductRequest


    // Phương thức thiết lập Category và Package cho Product
    private void setCategoryAndPackageForProduct(ProductRequest request, Products products) {
        if(request.getCategoriesId() !=null){
            Categories categories = categoryRepository.findById(request.getCategoriesId()).orElseThrow(
                    () -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy category với ID: " + request.getCategoriesId())
            );
            products.setCategoriesId(categories.getId());
        }


        if(request.getPackageId() !=null){
            Packages packages = packagesRepository.findById(request.getPackageId()).orElseThrow(
                    () -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy package với ID: " + request.getPackageId())
            );
            products.setPackageId(packages.getId());
        }
    }

    // Phương thức xây dựng ProductResponse từ Products
    private List<ProductsFeature> saveProductFeature(List<ProductFeatureRequest> productFeatureRequests ,Products saveProduct) {
        List<ProductsFeature> productsFeatureList = new ArrayList<>();


       for (ProductFeatureRequest productFeatureRequest : productFeatureRequests ){
           ProductsFeature productsFeature = new ProductsFeature();
           productsFeature.setName(productFeatureRequest.getName());
           productsFeature.setDescription(productFeatureRequest.getDescription());
           productsFeature.setProductsId(saveProduct.getId());

           productsFeatureList.add(productsFeature);
       }


        return productFeatureRepository.saveAll(productsFeatureList);

    }



}
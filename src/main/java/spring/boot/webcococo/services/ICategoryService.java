package spring.boot.webcococo.services;

import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.CategoriesResponse;

import java.util.List;

public interface ICategoryService {
     CategoriesResponse saveCategoryTree(CategoriesRequest request);


     void deleteCategoryTree(Integer id);

     CategoriesResponse updateCategoryTree(CategoriesRequest request , Integer id);

     CategoriesResponse getCategoryTree(Integer categoriesParentId);

     List<CategoriesResponse> getAllCategoryTree();

}

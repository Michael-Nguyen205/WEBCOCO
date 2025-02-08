package spring.boot.webcococo.services;

import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.CategoriesResponse;

import java.io.IOException;
import java.util.List;

public interface ICategoryService {
     CategoriesResponse saveCategoryTree(CategoriesRequest request,Integer languageId) throws NoSuchFieldException, IllegalAccessException;


     void deleteCategoryTree(Integer id);

     CategoriesResponse updateCategoryTree(CategoriesRequest request , Integer id);

     CategoriesResponse getCategoryTree(Integer categoriesParentId,String acceptLanguage);

     List<CategoriesResponse> getAllCategoryTree( String acceptLanguage);


     String createCategoriesImages(Integer categoriesId, MultipartFile file) throws IOException;


}

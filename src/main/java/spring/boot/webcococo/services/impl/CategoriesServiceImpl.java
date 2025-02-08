package spring.boot.webcococo.services.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.entities.Translation;
import spring.boot.webcococo.entities.TranslationKey;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CategoriesPojo;
import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.CategoriesResponse;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.repositories.I18nLanguageRepository;
import spring.boot.webcococo.services.ICategoryService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;
import spring.boot.webcococo.utils.FileStorageUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service

public class CategoriesServiceImpl extends BaseServiceImpl<Categories, Integer, CategoryRepository> implements ICategoryService {

    private final CategoryRepository categoriesRepository;
    private final ModelMapper modelMapper;
    private final EntityCascadeDeletionUtil deletionUtil;

    private final I18nLanguageServiceImpl i18nLanguageService;

    private  final FileStorageUtil fileStorageUtil;

    private final I18nLanguageRepository i18nLanguageRepository;
    private final TranslationServiceImpl translationService;

    public CategoriesServiceImpl(CategoryRepository repo, CategoryRepository categoriesRepository, ModelMapper modelMapper, EntityCascadeDeletionUtil deletionUtil, I18nLanguageServiceImpl i18nLanguageService, I18nLanguageRepository i18nLanguageRepository, TranslationKeyServiceImpl translationKeyService, FileStorageUtil fileStorageUtil, TranslationServiceImpl translationService) {
        super(repo); // Truyền repository vào lớp cha
        this.categoriesRepository = categoriesRepository;
        this.modelMapper = modelMapper;
        this.deletionUtil = deletionUtil;
        this.i18nLanguageService = i18nLanguageService;
        this.i18nLanguageRepository = i18nLanguageRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.translationService = translationService;
    }






//getAll cate theo mã ngôn ngữ
//get cate theo mã ngôn ngữ
//post tạo cate với ngôn ngữ mặc định
//delete xoá cate
//update cập nhật cate"field chính"





//query trả về tất cả các data categories kèm theo dữ liệu ngôn ngữ - get/categories/laguage
// , mình có thể sửa đổi trực tiếp các trường bình thường - /put/categories
// nhưng không chỉnh được {field}translationkey , khi click chi tiết vào
// nút fieldtranslation nó sẽ hiện các lô field theo mã ngôn ngữ  - get/tránslation/listfiedkey/{tranid}
    // và có thể edit được những lô nào của ngôn ngữ nào trống thì mình
    // có thể tạo thêm post/translation












//.tạo về list locale
//.update về list locale
//.get all về list locale
//.get về list locale
//.query locale trả về list model,model hiện list translation_key , translation_key hiện list translation.
//.                              update thông tin translation_key
//.                              delete           translation_key
//.                                                               ,  update thông tin translation
//.                                                               ,  delete thông tin translation


//. hàm tạo translation_key và translation theo locale và entity ()







    @Override
    @Transactional
    public CategoriesResponse saveCategoryTree(CategoriesRequest request,Integer  languageId)  {
        Categories parentCategories = new Categories();
        parentCategories = save(parentCategories);


        Integer  nameTranslationKeyId = translationService.createTranslationForNewEntity(parentCategories, request, languageId, "name");
        Integer isActiveTranslationKeyId = translationService.createTranslationForNewEntity(parentCategories,request,languageId,"isActive");


        parentCategories.setNameTranslationKeyId(nameTranslationKeyId);
        parentCategories.setIsActiveTranslationKeyId(isActiveTranslationKeyId);
        parentCategories = save(parentCategories);


        List<Categories> categoriesTree = updateCategoriesChild(request, parentCategories ,languageId);

        List<CategoriesPojo> categoriesPojoList = new ArrayList<>();
        for (Categories categories : categoriesTree) {

            CategoriesPojo categoriesPojo = new CategoriesPojo();
            categoriesPojo.setId(categories.getId());

            categoriesPojo.setName("value in translation");
            categoriesPojo.setParentId(categories.getParentId());
            categoriesPojoList.add(categoriesPojo);
        }


        return toCategoriesTreeResponse(categoriesPojoList, null);
    }


    @Transactional
    public List<Categories> updateCategoriesChild(CategoriesRequest request, Categories parentCategories ,Integer languageId) {
        List<Categories> categoriesList = new ArrayList<>();
        categoriesList.add(parentCategories);
        // Nếu có danh mục con, xử lý đệ quy
        if (request.getChildren() != null && !request.getChildren().isEmpty()) {
            for (CategoriesRequest childRequest : request.getChildren()) {
                Categories childCategory = new Categories();
                save(childCategory);
                Integer nameTranslationKeyId = translationService.createTranslationForNewEntity(childCategory, childRequest, languageId, "name");
                Integer isActiveTranslationKeyId = translationService.createTranslationForNewEntity(childCategory,request,languageId,"isActive");

             // Có thể là null nếu là danh mục mới
                childCategory.setNameTranslationKeyId(nameTranslationKeyId);
                childCategory.setIsActiveTranslationKeyId(isActiveTranslationKeyId);
                childCategory.setParentId(parentCategories.getId());
                log.error("childCategory:{}",childCategory);// Gán parentId là ID của cha
                save(childCategory);
                categoriesList.addAll(updateCategoriesChild(childRequest, childCategory ,languageId)); // Đệ quy và thêm vào danh sách
            }
        }
        return categoriesList;
    }


    @Transactional
    @Override
    public void deleteCategoryTree(Integer id) {
        List<Categories> allCategoriesEntity = categoriesRepository.findAll();
        Categories cateParent = categoriesRepository.findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        List<Categories> categoriesTree = toCategoriesTree(cateParent, allCategoriesEntity);
        categoriesRepository.deleteAll(categoriesTree);

        deletionUtil.deleteByParentId(id, "categories_id");
    }

    @Override
    public CategoriesResponse updateCategoryTree(CategoriesRequest request, Integer id) {
        return null;
    }

//
//
//    @Transactional
//    @Override
//    public CategoriesResponse updateCategoryTree(CategoriesRequest request, Integer id) {
//        try {
//            Categories parentCategories = findById(id)
//                    .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//
//            if (request.getName() != null) {
//                parentCategories.setName(request.getName());
//
//            }
//            I18Language i18Language = null;
//            if (request.getI18nLanguageId() != null) {
//                i18Language = getI18LanguageById(request.getI18nLanguageId());
//                parentCategories.setI18nLanguageId(i18Language.getId());
//            }
//
//
//            if (request.getIsActive() != null) {
//                parentCategories.setIsActive(request.getIsActive());
//            }
//
//
//            save(parentCategories);
//
//            // cập nhật các danh mục con vào DB
//            List<Categories> categoriesTreeUpdate = new ArrayList<>();
//            List<Categories> categoriesListToDelete = new ArrayList<>();
//            //nếu có phần tủ con thì cập nhật không thì chỉ cập nhật phần tử đơn lẻ
//            if (request.getChildren() != null) {
//                categoriesTreeUpdate = updateChildCategoriesTree(request.getChildren(), parentCategories, categoriesListToDelete);
//            } else {
//                categoriesTreeUpdate.add(parentCategories);
//            }
//
//            log.error("categoriesTree 1:{}", categoriesTreeUpdate);
//
//
//            if (!categoriesListToDelete.isEmpty()) {
//                categoriesRepository.deleteAll(categoriesListToDelete);
//            }
//
//            log.error("categoriesTree 2:{}", categoriesTreeUpdate);
//
//
//            List<CategoriesPojo> categoriesPojoList = new ArrayList<>();
//            for (Categories categories : categoriesTreeUpdate) {
//                CategoriesPojo categoriesPojo = new CategoriesPojo();
//                categoriesPojo.setId(categories.getId());
//                categoriesPojo.setIsActive(categories.getIsActive());
//                categoriesPojo.setI18nLanguageId(categories.getI18nLanguageId());
//                categoriesPojo.setName(categories.getName());
//                categoriesPojo.setParentId(categories.getParentId());
//                categoriesPojoList.add(categoriesPojo);
//            }
//
//            log.error("categoriesPojoList:{}", categoriesPojoList);
//
//
//
//            return toCategoriesTreeResponse(categoriesPojoList, null);
//        } catch (AppException e) {
//            // Handle specific AppException
//            log.error("Error updating category tree: {}", e.getMessage());
//            throw e;  // Optionally rethrow the exception if you want to propagate it further
//        } catch (Exception e) {
//            // Catch any other exceptions
//            throw e; // Or any other custom error code
//        }
//    }
//
//
//    @Transactional
//    public List<Categories> updateChildCategoriesTree(
//            List<CategoriesRequest> childrenRequests,
//            Categories parentCategory,
//            List<Categories> CategoriesListToDelete
//    ) {
//        List<Categories> updatedCategorieTree = new ArrayList<>();
//        updatedCategorieTree.add(parentCategory);
//        // Nếu có danh mục con
//        if (childrenRequests != null && !childrenRequests.isEmpty()) {
//            for (CategoriesRequest childRequest : childrenRequests) {
//                Categories childCategory;
//                // nếu có id cụ thể
//                if (childRequest.getId() != null) {
//                    // Tìm danh mục con đã tồn tại
//                    childCategory = findById(childRequest.getId())
//                            .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND,
//                                    "Không tìm thấy danh mục với ID: " + childRequest.getId()));
//
//                    // Kiểm tra nếu danh mục chỉ có id thì cho vào list cần xoá
//                    if (childRequest.getName() == null && childRequest.getParentId() == null && childRequest.getIsActive() == null && childRequest.getI18nLanguageId() == null) {
//                        // Thêm danh mục cần xóa vào danh sách
//
//                        CategoriesListToDelete.add(childCategory);
//                        //Nếu có các thông tin khác thì cập nhật
//                    } else {
//                        if (parentCategory.getI18nLanguageId() != null) {
//                            childCategory.setI18nLanguageId(parentCategory.getI18nLanguageId());
//                        }
//                        if (childRequest.getName() != null) {
//                            childCategory.setName(childRequest.getName());
//                        }
//                        if (childRequest.getIsActive() != null) {
//                            childCategory.setIsActive(childRequest.getIsActive());
//                        }
//
//                        childCategory.setParentId(parentCategory.getId());
//                    }
//                } else {
//                    // Tạo mới danh mục nếu không có ID
//                    childCategory = new Categories();
//                    if (parentCategory.getI18nLanguageId() != null) {
//                        childCategory.setI18nLanguageId(parentCategory.getI18nLanguageId());
//                    }
//                    if (childRequest.getName() != null) {
//                        childCategory.setName(childRequest.getName());
//                    }
//                    if (childRequest.getIsActive() != null) {
//                        childCategory.setIsActive(childRequest.getIsActive());
//                    }
//                    childCategory.setParentId(parentCategory.getId());
//                }
//
//                // Cập nhật hoặc tạo thông tin danh mục
//                childCategory = save(childCategory);
//                // Đệ quy xử lý danh mục con
//                updatedCategorieTree.addAll(updateChildCategoriesTree(childRequest.getChildren(), childCategory, CategoriesListToDelete));
//
//            }
//        }
//
//        return updatedCategorieTree;
//    }
//


    @Transactional
    @Override
    public CategoriesResponse getCategoryTree(Integer categoriesParentId,String acceptLanguage) {
        List<Tuple> categoriesNative = categoriesRepository.findCategoryTree(categoriesParentId,acceptLanguage);

        List<CategoriesPojo> categoriesPojoTree = new ArrayList<>();
        for (Tuple nativeData : categoriesNative) {
            CategoriesPojo categoriesPojo = new CategoriesPojo();
            categoriesPojo.setId((Integer) nativeData.get("c_id"));
            categoriesPojo.setName((String) nativeData.get("c_name"));
            categoriesPojo.setI18nLanguageId((Integer) nativeData.get("l_id"));
            categoriesPojo.setLanguageCode((String) nativeData.get("l_code"));
            categoriesPojo.setIsActive(
                    nativeData.get("c_is_active") != null &&
                            Boolean.parseBoolean(nativeData.get("c_is_active").toString())
            );

            categoriesPojo.setParentId((Integer) nativeData.get("c_parent_id"));
//            categoriesPojo.setLevel((Integer) nativeData.get("level"));
//            Object levelObj = nativeData.get("level");
//            if (levelObj != null) {
//                categoriesPojo.setLevel(((Number) levelObj).intValue());  // Chuyển đổi từ bất kỳ Number sang Integer
//            } else {
//                categoriesPojo.setLevel(null);  // Nếu null thì set null
//            }
            log.error("categoriesPojooo:{}",categoriesPojo);
            categoriesPojoTree.add(categoriesPojo);
        }
        log.error("categoriesTree:{}", categoriesPojoTree);
        return toCategoriesTreeResponse(categoriesPojoTree, null);
    }


    @Transactional
    @Override
    public List<CategoriesResponse> getAllCategoryTree(String acceptLanguage) {
        List<Tuple> categoriesNative = categoriesRepository.getAllCategoryTreeByLanguage(acceptLanguage);

        List<CategoriesPojo> categoriesPojoList = new ArrayList<>();
        for (Tuple nativeData : categoriesNative) {
            CategoriesPojo categoriesPojo = new CategoriesPojo();

            categoriesPojo.setIsActive(
                    nativeData.get("c_is_active") != null &&
                            Boolean.parseBoolean(nativeData.get("c_is_active").toString())
            );

            categoriesPojo.setId((Integer) nativeData.get("c_id"));
            categoriesPojo.setName((String) nativeData.get("c_name"));
            categoriesPojo.setI18nLanguageId((Integer) nativeData.get("l_id"));
            categoriesPojo.setLanguageCode((String) nativeData.get("l_code"));
            categoriesPojo.setParentId((Integer) nativeData.get("c_parent_id"));

            categoriesPojoList.add(categoriesPojo);

        }

        log.error("categoriesList:{}", categoriesPojoList);
        List<List<CategoriesPojo>> listCategoriesPojoTree = toListCategoriesPojoTree(categoriesPojoList);
        log.error("listCategoriesPojoTree:{}", listCategoriesPojoTree);
        List<CategoriesResponse> listCategoriesResponse = new ArrayList<>();


        for (List<CategoriesPojo> categoriesPojoTree : listCategoriesPojoTree) {
            CategoriesResponse categoriesResponse = toCategoriesTreeResponse(categoriesPojoTree, null);
            log.error("categoriesResponse:{}", categoriesResponse);
            listCategoriesResponse.add(categoriesResponse);
        }

        return listCategoriesResponse;
    }


    List<List<CategoriesPojo>> toListCategoriesPojoTree(List<CategoriesPojo> allCategoriesPojo) {
        log.error("vaoToListCategoriesTree");
        List<List<CategoriesPojo>> listCategoriesPojoTree = new ArrayList<>();
        List<CategoriesPojo> listCatePojoParent = allCategoriesPojo
                .stream().filter(categories -> categories.getParentId() == null).toList();
        log.error("listCatePojoParent:{}", listCatePojoParent);
        for (CategoriesPojo categoriesPojoParent : listCatePojoParent) {
            List<CategoriesPojo> categoriesPojoTree =
                    toCategoriesTreePojo(categoriesPojoParent, allCategoriesPojo);
            log.error("categoriesTree:{}", categoriesPojoTree);
            listCategoriesPojoTree.add(categoriesPojoTree);
        }
        return listCategoriesPojoTree;
    }






    public List<CategoriesPojo> toCategoriesTreePojo(CategoriesPojo categoriesParent, List<CategoriesPojo> allCategoriesPojo) {
        // Danh sách để lưu trữ tất cả các danh mục (gồm cha và các nhánh con)
        List<CategoriesPojo> result = new ArrayList<>();
        // Thêm danh mục cha vào danh sách
        result.add(categoriesParent);
        // Lọc danh mục con có parentId trùng với id của danh mục cha
        List<CategoriesPojo> childCategoriePojoList = allCategoriesPojo.stream()
                .filter(category -> category.getParentId() != null && category.getParentId().equals(categoriesParent.getId()))
                .toList();
        // Với mỗi danh mục con, gọi đệ quy để lấy toàn bộ cây danh mục con
        for (CategoriesPojo child : childCategoriePojoList) {
            result.addAll(toCategoriesTreePojo(child, allCategoriesPojo)); // Đệ quy và thêm tất cả các nhánh con vào danh sách
        }
        return result;
    }









    @Transactional
    public CategoriesResponse toCategoriesTreeResponse(List<CategoriesPojo> categoriesPojoTree, CategoriesPojo parentCategoryTree) {
        // Tạo đối tượng CategoriesResponse cho danh mục gốc
        CategoriesResponse response = new CategoriesResponse();
        // Nếu parentCategory là null, ta cần tìm danh mục gốc (cha)
        log.error("categoriesPojoTreetoCategoriesTreeResponse:{}",categoriesPojoTree);
        if (parentCategoryTree == null) {
            parentCategoryTree = categoriesPojoTree.stream()
                    .filter(category -> category.getParentId() == null) // Tìm danh mục cha
                    .findFirst() // Lấy danh mục đầu tiên
                    .orElseThrow(() -> new RuntimeException("Parent category not found")); // Nếu không tìm thấy thì ném lỗi

            // Set thông tin cho danh mục cha
            response.setId(parentCategoryTree.getId());
            response.setName(parentCategoryTree.getName());
            response.setIsActive(parentCategoryTree.getIsActive());
            response.setI18nLanguageId(parentCategoryTree.getI18nLanguageId());
            response.setLanguageCode(parentCategoryTree.getLanguageCode());
            response.setParentId(parentCategoryTree.getParentId());
            response.setIsActive(parentCategoryTree.getIsActive());

        } else {
            // Nếu parentCategory không phải là null, tức là đang xử lý đệ quy, set thông tin của parent
            response.setId(parentCategoryTree.getId());
            response.setName(parentCategoryTree.getName());
            response.setIsActive(parentCategoryTree.getIsActive());
            response.setI18nLanguageId(parentCategoryTree.getI18nLanguageId());
            response.setLanguageCode(parentCategoryTree.getLanguageCode());
            response.setParentId(parentCategoryTree.getParentId());
            response.setIsActive(parentCategoryTree.getIsActive());
        }

        // Tìm các danh mục con của danh mục gốc
        CategoriesPojo finalparentCategoryTree = parentCategoryTree;
        List<CategoriesPojo> childrenList = categoriesPojoTree.stream()
                .filter(category -> category.getParentId() != null && category.getParentId().equals(finalparentCategoryTree.getId())) // Đã thêm dấu ngoặc đóng
                .toList();

        // Nếu có danh mục con, xử lý đệ quy
        if (!childrenList.isEmpty()) {
            List<CategoriesResponse> childrenResponses = childrenList.stream()
                    .map(child -> toCategoriesTreeResponse(categoriesPojoTree, child)) // Đệ quy xử lý danh mục con
                    .toList();
            response.setChildren(childrenResponses); // Gán danh mục con vào
        } else {
            response.setChildren(null); // Không có con, trả về danh sách rỗng
        }
        return response;
    }


    List<List<Categories>> toListCategoriesTree(List<Categories> allCategoriesEntity) {
        log.error("vaoToListCategoriesTree");
        List<List<Categories>> listCategoriesTree = new ArrayList<>();
        List<Categories> listCateParent = allCategoriesEntity.stream().filter(categories -> categories.getParentId() == null).toList();
        log.error("listCateParent:{}", listCateParent);
        for (Categories cateParent : listCateParent) {
            List<Categories> categoriesTree = toCategoriesTree(cateParent, allCategoriesEntity);
            log.error("categoriesTree:{}", categoriesTree);

            listCategoriesTree.add(categoriesTree);
        }
        return listCategoriesTree;
    }


    public List<Categories> toCategoriesTree(Categories categoriesParent, List<Categories> allCategoriesEntity) {
        // Danh sách để lưu trữ tất cả các danh mục (gồm cha và các nhánh con)
        List<Categories> result = new ArrayList<>();
        // Thêm danh mục cha vào danh sách
        result.add(categoriesParent);

        // Lọc danh mục con có parentId trùng với id của danh mục cha
        List<Categories> childCategorieList = allCategoriesEntity.stream()
                .filter(category -> category.getParentId() != null && category.getParentId().equals(categoriesParent.getId()))
                .toList();
        // Với mỗi danh mục con, gọi đệ quy để lấy toàn bộ cây danh mục con
        for (Categories child : childCategorieList) {
            result.addAll(toCategoriesTree(child, allCategoriesEntity)); // Đệ quy và thêm tất cả các nhánh con vào danh sách
        }
        return result;
    }










    @org.springframework.transaction.annotation.Transactional
    @Override
    public String createCategoriesImages(Integer categoriesId, MultipartFile file) throws IOException {
        try {


            Categories existingCategories = findById(categoriesId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));



            // Kiểm tra kích thước file và định dạng
            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                throw new AppException(ErrorCodeEnum.UPLOAD_IMAGES_FILE_LARGE);
            }


            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new AppException(ErrorCodeEnum.UPLOAD_IMAGES_FILE_LARGE);
            }

            // Lưu file và cập nhật thumbnail trong DTO
            String filename = fileStorageUtil.storeFile(file); // Thay thế hàm này với code của bạn để lưu file
            //lưu vào đối tượng product trong DB
            if (filename == null || filename.isEmpty()) {
                throw new AppException(ErrorCodeEnum.FILE_UPLOAD_FAILED);
            }



            existingCategories.setImage(filename);

           save(existingCategories);

            return filename;
        } catch (Exception e) {
            throw e;
        }
    }






}

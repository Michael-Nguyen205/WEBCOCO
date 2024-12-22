package spring.boot.webcococo.services.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CategoriesPojo;
import spring.boot.webcococo.models.requests.CategoriesRequest;
import spring.boot.webcococo.models.response.CategoriesResponse;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.services.ICategoryService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service

public class CategoriesServiceImpl extends BaseServiceImpl<Categories, Integer, CategoryRepository> implements ICategoryService {

    private final CategoryRepository categoriesRepository;
    private final ModelMapper modelMapper;
    private final EntityCascadeDeletionUtil deletionUtil;


    public CategoriesServiceImpl(CategoryRepository repo, CategoryRepository categoriesRepository, ModelMapper modelMapper, EntityCascadeDeletionUtil deletionUtil) {
        super(repo); // Truyền repository vào lớp cha
        this.categoriesRepository = categoriesRepository;
        this.modelMapper = modelMapper;
        this.deletionUtil = deletionUtil;
    }

    @Override
    @Transactional
    public CategoriesResponse saveCategoryTree(CategoriesRequest request) {
        Categories parentCategories = new Categories();
        parentCategories.setName(request.getName());
        parentCategories = save(parentCategories);
        List<Categories> categoriesSet = toCategoriesTree(request, parentCategories);
        // Lưu tất cả danh mục con vào DB
//        saveAll(categoriesSet);

        // Lưu vào DB
        return toCategoriesTreeResponse(categoriesSet, null);
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


    @Transactional
    public List<Categories> toCategoriesTree(CategoriesRequest request, Categories parentCategories) {
        List<Categories> categoriesList = new ArrayList<>();
        categoriesList.add(parentCategories);
        // Nếu có danh mục con, xử lý đệ quy
        if (request.getChildren() != null && !request.getChildren().isEmpty()) {
            for (CategoriesRequest child : request.getChildren()) {
                Categories childCategory = new Categories();
                childCategory.setId(child.getId()); // Có thể là null nếu là danh mục mới
                childCategory.setName(child.getName());
                childCategory.setParentId(parentCategories.getId()); // Gán parentId là ID của cha
                save(childCategory);
                categoriesList.addAll(toCategoriesTree(child, childCategory)); // Đệ quy và thêm vào danh sách
            }
        }

        return categoriesList;
    }


    @Transactional
    @Override
    public CategoriesResponse updateCategoryTree(CategoriesRequest request, Integer id) {
        Categories parentCategories = findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        parentCategories.setName(request.getName());
        parentCategories = save(parentCategories);
        // Lưu tất cả danh mục con vào DB
        List<Categories> categoriesListToDelete = new ArrayList<>();
        List<Categories> categoriesTree = updateChildCategoriesTree(request.getChildren(), parentCategories, categoriesListToDelete);
        log.error("categoriesTree 1:{}",categoriesTree);
        categoriesTree.add(parentCategories);
        categoriesRepository.deleteAll(categoriesListToDelete);
        log.error("categoriesTree 2:{}",categoriesTree);
        return toCategoriesTreeResponse(categoriesTree, null);
    }

    @Transactional
    public List<Categories> updateChildCategoriesTree(
            List<CategoriesRequest> childrenRequests,
            Categories parentCategory,
            List<Categories> CategoriesListToDelete
    ) {
        List<Categories> updatedCategories = new ArrayList<>();
        updatedCategories.add(parentCategory);

        // Nếu có danh mục con
        if (childrenRequests != null && !childrenRequests.isEmpty()) {
            for (CategoriesRequest childRequest : childrenRequests) {
                Categories childCategory;

                if (childRequest.getId() != null) {
                    // Tìm danh mục con đã tồn tại
                    childCategory = findById(childRequest.getId())
                            .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND,
                                    "Không tìm thấy danh mục với ID: " + childRequest.getId()));

                    // Kiểm tra nếu danh mục cần xóa
                    if (childRequest.getName() == null && childRequest.getParentId()==null) {
                        // Thêm danh mục cần xóa vào danh sách
                        CategoriesListToDelete.add(childCategory);
                    }else {
                        childCategory.setName(childRequest.getName());
                        childCategory.setParentId(parentCategory.getId());
                    }

                } else {
                    // Tạo mới danh mục nếu không có ID
                    childCategory = new Categories();
                    childCategory.setName(childRequest.getName());
                    childCategory.setParentId(parentCategory.getId());
                }

                // Cập nhật thông tin danh mục

                childCategory = save(childCategory);
                // Đệ quy xử lý danh mục con
                updatedCategories.addAll(updateChildCategoriesTree(childRequest.getChildren(), childCategory, CategoriesListToDelete));

            }
        }

        return updatedCategories;
    }




    @Transactional
    @Override
    public CategoriesResponse getCategoryTree(Integer categoriesParentId) {
        List<Tuple> categoriesNative = categoriesRepository.findCategoryTree(categoriesParentId);

        List<CategoriesPojo> categoriesPojoList = new ArrayList<>();
        for (Tuple nativeData : categoriesNative) {
            CategoriesPojo categoriesPojo = new CategoriesPojo();
            categoriesPojo.setId((Integer) nativeData.get("id"));
            categoriesPojo.setName((String) nativeData.get("name"));
            categoriesPojo.setParentId((Integer) nativeData.get("parent_id"));
//            categoriesPojo.setLevel((Integer) nativeData.get("level"));

            Object levelObj = nativeData.get("level");
            if (levelObj != null) {
                categoriesPojo.setLevel(((Number) levelObj).intValue());  // Chuyển đổi từ bất kỳ Number sang Integer
            } else {
                categoriesPojo.setLevel(null);  // Nếu null thì set null
            }
            categoriesPojoList.add(categoriesPojo);

        }


        if (modelMapper.getTypeMap(CategoriesPojo.class, Categories.class) == null) {
            modelMapper.typeMap(CategoriesPojo.class, Categories.class);
        }
        List<Categories> categoriesTree = new ArrayList<>();
        for (CategoriesPojo pojo : categoriesPojoList) {
            Categories categories = new Categories();
            modelMapper.map(pojo, categories);
            categoriesTree.add(categories);
        }



        log.error("categoriesTree:{}",categoriesTree);
        return toCategoriesTreeResponse(categoriesTree, null);
    }



    @Transactional
    public CategoriesResponse toCategoriesTreeResponse(List<Categories> categoriesTree, Categories parentCategory) {
        // Tạo đối tượng CategoriesResponse cho danh mục gốc
        CategoriesResponse response = new CategoriesResponse();

        // Nếu parentCategory là null, ta cần tìm danh mục gốc (cha)
        if (parentCategory == null) {
            parentCategory = categoriesTree.stream()
                    .filter(category -> category.getParentId() == null) // Tìm danh mục cha
                    .findFirst() // Lấy danh mục đầu tiên
                    .orElseThrow(() -> new RuntimeException("Parent category not found")); // Nếu không tìm thấy thì ném lỗi

            // Set thông tin cho danh mục cha
            response.setId(parentCategory.getId());
            response.setName(parentCategory.getName());
            response.setParentId(parentCategory.getParentId());

        } else {
            // Nếu parentCategory không phải là null, tức là đang xử lý đệ quy, set thông tin của parent
            response.setId(parentCategory.getId());
            response.setName(parentCategory.getName());
            response.setParentId(parentCategory.getParentId());
        }

        // Tìm các danh mục con của danh mục gốc
        Categories finalParentCategory = parentCategory;
        List<Categories> childrenList = categoriesTree.stream()
                .filter(category -> category.getParentId() != null && category.getParentId().equals(finalParentCategory.getId())) // Đã thêm dấu ngoặc đóng
                .toList();

        // Nếu có danh mục con, xử lý đệ quy
        if (!childrenList.isEmpty()) {
            List<CategoriesResponse> childrenResponses = childrenList.stream()
                    .map(child -> toCategoriesTreeResponse(categoriesTree, child)) // Đệ quy xử lý danh mục con
                    .toList();
            response.setChildren(childrenResponses); // Gán danh mục con vào
        } else {
            response.setChildren(null); // Không có con, trả về danh sách rỗng
        }
        return response;
    }


    @Transactional
    @Override
    public List<CategoriesResponse> getAllCategoryTree() {
        List<Categories> allCategoriesEntity = categoriesRepository.findAll();
        log.error("categoriesList:{}", allCategoriesEntity);
        List<List<Categories>> listCategoriesTree = toListCategoriesTree(allCategoriesEntity);
        log.error("listCategoriesTree:{}", listCategoriesTree);

        List<CategoriesResponse> listCategoriesResponse = new ArrayList<>();
        for (List<Categories> categoriesTree : listCategoriesTree) {
            CategoriesResponse categoriesResponse = toCategoriesTreeResponse(categoriesTree, null);
            log.error("categoriesResponse:{}", categoriesResponse);
            listCategoriesResponse.add(categoriesResponse);
        }

        return listCategoriesResponse;
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


}

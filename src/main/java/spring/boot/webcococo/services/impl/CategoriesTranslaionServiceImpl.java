package spring.boot.webcococo.services.impl;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.models.mapping.CategoriesTranslationsMapping;
import spring.boot.webcococo.models.response.CategoryTranslationsResponse;
import spring.boot.webcococo.models.response.TranslationResponse;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.repositories.I18nLanguageRepository;
import spring.boot.webcococo.services.ICategoriesTranslationService;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class CategoriesTranslaionServiceImpl extends BaseServiceImpl<Categories, Integer, CategoryRepository> implements ICategoriesTranslationService {
    private final CategoryRepository categoriesRepository;

    private final I18nLanguageRepository languageRepository;

    CategoriesTranslaionServiceImpl(CategoryRepository repo, CategoryRepository categoriesRepository, I18nLanguageRepository i18nLanguageRepository) {
        super(repo);
        this.categoriesRepository = categoriesRepository;
        this.languageRepository = i18nLanguageRepository;
    }


    @Override
    public List<CategoryTranslationsResponse> getAllCategoriesAndTranslationFiels() {
        List<CategoryTranslationsResponse> responses = new ArrayList<>();
        try {
            // Lấy dữ liệu từ repository
            List<Tuple> categoriesNativeData = categoriesRepository.getAllCategoriesWithTranslations();
            List<CategoriesTranslationsMapping> categoriesTranslationsMappingList = new ArrayList<>();

            // Ánh xạ dữ liệu từ Tuple sang CategoriesTranslationsMapping
            for (Tuple nativeData : categoriesNativeData) {
                CategoriesTranslationsMapping categoriesTranslationsMapping = new CategoriesTranslationsMapping();

                // Thông tin danh mục
                categoriesTranslationsMapping.setCategoryId((Integer) nativeData.get("c_id"));
                categoriesTranslationsMapping.setParentCategoryId((Integer) nativeData.get("c_parent_id"));

                // Thông tin ngôn ngữ
                categoriesTranslationsMapping.setLanguageId((Integer) nativeData.get("l_id"));
                categoriesTranslationsMapping.setLanguageCode((String) nativeData.get("l_code"));

                // Thông tin name translation
                categoriesTranslationsMapping.setNameTranslationKeyId((Integer) nativeData.get("tk_name_id"));
                categoriesTranslationsMapping.setNameTranslationKey((String) nativeData.get("tk_name_key"));
                categoriesTranslationsMapping.setNameTranslationId((String) nativeData.get("t_name_id"));
                categoriesTranslationsMapping.setNameTranslationContent((String) nativeData.get("t_name_content"));

                // Thông tin is_active translation
                categoriesTranslationsMapping.setIsActiveTranslationKeyId((Integer) nativeData.get("tk_is_active_id"));
                categoriesTranslationsMapping.setIsActiveTranslationKey((String) nativeData.get("tk_is_active_key"));
                categoriesTranslationsMapping.setIsActiveTranslationId((String) nativeData.get("t_is_active_id"));
                categoriesTranslationsMapping.setIsActiveTranslationContent((String) nativeData.get("t_is_active_content"));

                categoriesTranslationsMappingList.add(categoriesTranslationsMapping);
            }

            log.info("categoriesTranslationsMappingList: {}", categoriesTranslationsMappingList);
            log.info("categoriesTranslationsMappingListSize: {}", categoriesTranslationsMappingList.size());

            // Chuyển đổi sang danh sách cây
            List<Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>>> listCateTree = toListCategoriesTree(categoriesTranslationsMappingList);


            // Tạo danh sách phản hồi
            for (Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>> entry : listCateTree) {


                log.error("entryCategories:{}", entry);
                responses.add(toCategoriesTreeResponse(entry, null));
            }
        } catch (Exception e) {
            // Ghi log lỗi và trả về danh sách rỗng
            throw e;
        }
        return responses;
    }


    List<Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>>> toListCategoriesTree(List<CategoriesTranslationsMapping> allCateMap) {
        log.error("vaoToListCategoriesTree");
        List<Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>>> listCategoriesPojoTree = new ArrayList<>();

        Map<Integer, List<CategoriesTranslationsMapping>> parentCate = allCateMap.stream()
                .filter(category -> category.getParentCategoryId() == null) // Lọc các danh mục có parentId khác null
                .collect(Collectors.groupingBy(CategoriesTranslationsMapping::getCategoryId)); // Group theo parentId

        log.error("parentCate.values().size():{}", parentCate.values().size());


        parentCate.values().size();
        for (Map.Entry<Integer, List<CategoriesTranslationsMapping>> entryParents : parentCate.entrySet()) {

            log.error("entryParents:{}", entryParents);
            Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>> categoriesTree = toCategoriesTree(entryParents, allCateMap);
            listCategoriesPojoTree.add(categoriesTree);

        }
        return listCategoriesPojoTree;
    }


    public Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>> toCategoriesTree(Map.Entry<Integer, List<CategoriesTranslationsMapping>> entryParent, List<CategoriesTranslationsMapping> allCategoriesPojo) {
        // Danh sách để lưu trữ tất cả các danh mục (gồm cha và các nhánh con)
        Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>> result = new HashSet<>();

        // Thêm danh mục cha vào danh sách
        result.add(entryParent);

        // Chuyển allCategoriesPojo thành một Map

        Map<Integer, List<CategoriesTranslationsMapping>> listCateChildMap = allCategoriesPojo.stream()
                .filter(category -> category.getParentCategoryId() != null && category.getParentCategoryId().equals(entryParent.getKey())) // Lọc các danh mục có parentId khác null
                .collect(Collectors.groupingBy(CategoriesTranslationsMapping::getCategoryId)); // Group theo parentId


        log.error("childTree.size():{}", listCateChildMap.values().size());

        log.error("childTrees:{}", listCateChildMap.values());


        // Với mỗi danh mục con, gọi đệ quy để lấy toàn bộ cây danh mục con
        for (Map.Entry<Integer, List<CategoriesTranslationsMapping>> entryChild : listCateChildMap.entrySet()) {
            result.addAll(toCategoriesTree(entryChild, allCategoriesPojo)); // Đệ quy và thêm tất cả các nhánh con vào danh sách
        }

        return result;
    }


    @Transactional
    public CategoryTranslationsResponse toCategoriesTreeResponse(Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>>
                                                                         categoriesTrees, Map.Entry<Integer, List<CategoriesTranslationsMapping>> parentCategoryTree) {
        // Tạo đối tượng CategoriesResponse cho danh mục gốc
        CategoryTranslationsResponse response = new CategoryTranslationsResponse();
        // Nếu parentCategory là null, ta cần tìm danh mục gốc (cha)
        log.error("categoriesPojoTreetoCategoriesTreeResponse:{}", categoriesTrees);
        if (parentCategoryTree == null) {

// tìm các lô cha
            parentCategoryTree = categoriesTrees.stream()
                    .filter(entry -> entry.getValue().stream()
                            .anyMatch(category -> category.getParentCategoryId() == null))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));


            List<CategoriesTranslationsMapping> categories = parentCategoryTree.getValue();
            CategoriesTranslationsMapping firtCate = categories.getFirst();
            // Set thông tin cho danh mục cha
            response.setId(parentCategoryTree.getKey());
//            response.setName(parentCategoryTree.getName());
//            response.setIsActive(parentCategoryTree.getIsActive());
            response.setLanguages(mapToCategoryLanguageList(categories));
            response.setParentId(firtCate.getParentCategoryId());


        } else {
            List<CategoriesTranslationsMapping> categories = parentCategoryTree.getValue();
            CategoriesTranslationsMapping firtCate = categories.getFirst();
            // Nếu parentCategory không phải là null, tức là đang xử lý đệ quy, set thông tin của parent
            response.setId(parentCategoryTree.getKey());
//            response.setName(parentCategoryTree.getName());
//            response.setIsActive(parentCategoryTree.getIsActive());
            response.setLanguages(mapToCategoryLanguageList(categories));
            response.setParentId(firtCate.getParentCategoryId());
        }


        // Tìm các danh mục con của danh mục gốc
        Map.Entry<Integer, List<CategoriesTranslationsMapping>> finalparentCategoryTree = parentCategoryTree;
        Set<Map.Entry<Integer, List<CategoriesTranslationsMapping>>> childrenList = categoriesTrees.stream()
                .filter(entry -> entry.getValue().stream()
                        .anyMatch(categories -> Objects.equals(categories.getParentCategoryId(), finalparentCategoryTree.getKey()))
                )
                .collect(Collectors.toSet());


        // Nếu có danh mục con, xử lý đệ quy
        if (!childrenList.isEmpty()) {
            List<CategoryTranslationsResponse> childrenResponses = childrenList.stream()
                    .map(entryChild -> toCategoriesTreeResponse(categoriesTrees, entryChild)) // Đệ quy xử lý danh mục con
                    .toList();

            response.setChildren(childrenResponses); // Gán danh mục con vào
        } else {
            response.setChildren(null); // Không có con, trả về danh sách rỗng
        }
        return response;
    }






    private List<CategoryTranslationsResponse.CategoryLanguage> mapToCategoryLanguageList(List<CategoriesTranslationsMapping> categories) {
        // Loại bỏ trùng lặp dựa trên `languageId`



        List<CategoriesTranslationsMapping> uniqueCategories = new ArrayList<>(categories.stream()
                .collect(Collectors.toMap(
                        CategoriesTranslationsMapping::getLanguageId,
                        category -> category,
                        (existing, replacement) -> existing
                ))
                .values());




        List<I18Language> languageList = languageRepository.findAll();



        log.error("uniqueCategories1:{}",uniqueCategories);


        languageList.stream()
                .filter(language -> uniqueCategories.stream().noneMatch(category ->   category.getLanguageId() != null && category.getLanguageId().equals(language.getId())))
                .map(language ->{
                    CategoriesTranslationsMapping takeTranslationKey = uniqueCategories.getFirst();
                    CategoriesTranslationsMapping catemappinglocale = new CategoriesTranslationsMapping();
                    catemappinglocale.setIsActiveTranslationKeyId(takeTranslationKey.getIsActiveTranslationKeyId());
                    catemappinglocale.setIsActiveTranslationKey(takeTranslationKey.getIsActiveTranslationKey());

                    catemappinglocale.setNameTranslationId(takeTranslationKey.getNameTranslationId());
                    catemappinglocale.setNameTranslationKey(takeTranslationKey.getNameTranslationKey());

                    catemappinglocale.setLanguageId(language.getId());
                    catemappinglocale.setLanguageCode(language.getCode());
                    return catemappinglocale;
                })
                .forEach(uniqueCategories::add);


//languageList.stream()
//        .filter(language -> uniqueCategories.stream().noneMatch(category -> category.getLanguageId().equals(language.getId())))
//            .map(language -> new CategoriesTranslationsMapping(
//            null, // categoryId là null
//                    null, // parentCategoryId là null
//            language.getId(), // languageId từ languageList
//            language.getCode(), // languageCode từ languageList
//                    null, // nameTranslationKeyId, có thể null
//                    null, // isActiveTranslationKeyId, có thể null
//                    null, // các trường khác nếu cần
//                    null  // hoặc giá trị mặc định
//                    ))
//                    .forEach(uniqueCategories::add);


         log.error("uniqueCategories:{}",uniqueCategories);


        // Chuyển đổi từng đối tượng CategoriesTranslationsMapping thành CategoryLanguage
        return uniqueCategories.stream().map(this::mapToCategoryLanguage).toList();
    }


    private CategoryTranslationsResponse.CategoryLanguage mapToCategoryLanguage(CategoriesTranslationsMapping cateLocale) {
        // Tạo đối tượng CategoryLanguage
        CategoryTranslationsResponse.CategoryLanguage languageInfo = new CategoryTranslationsResponse().new CategoryLanguage();
        // Gán dữ liệu ngôn ngữ
        languageInfo.setLanguageId(cateLocale.getLanguageId());
        languageInfo.setLanguageCode(cateLocale.getLanguageCode());
        // Tạo và thiết lập NameTranslationFields

        List<TranslationResponse> translationResponseList = new ArrayList<>();
        TranslationResponse  nameTranslationResponse = new TranslationResponse();

        nameTranslationResponse.setTranslationKeyId(cateLocale.getNameTranslationKeyId());
        nameTranslationResponse.setTranslationKey(cateLocale.getNameTranslationKey());
        nameTranslationResponse.setTranslationId(cateLocale.getNameTranslationId());
        nameTranslationResponse.setTranslationContent(cateLocale.getNameTranslationContent());
        translationResponseList.add(nameTranslationResponse);


        TranslationResponse isActivetranslation =new TranslationResponse();
        isActivetranslation.setTranslationKeyId(cateLocale.getIsActiveTranslationKeyId());
        isActivetranslation.setTranslationKey(cateLocale.getIsActiveTranslationKey());
        isActivetranslation.setTranslationId(cateLocale.getIsActiveTranslationId());
        isActivetranslation.setTranslationContent( cateLocale.getIsActiveTranslationContent());
        translationResponseList.add(isActivetranslation);

        languageInfo.setTranslationResponse(translationResponseList);
        return languageInfo;
    }

//    private CategoryTranslationsResponse.CategoryLanguage.NameTranslationFields mapNameTranslationFields(CategoriesTranslationsMapping object) {
//        CategoryTranslationsResponse.CategoryLanguage.NameTranslationFields translationFields = new CategoryTranslationsResponse().new CategoryLanguage().new NameTranslationFields();
//        translationFields.setTranslationKeyId(object.getNameTranslationKeyId());
//        translationFields.setTranslationKey(object.getNameTranslationKey());
//        translationFields.setTranslationId(object.getNameTranslationId());
//        translationFields.setContent(object.getNameTranslationContent());
//        return translationFields;
//    }
//
//    private CategoryTranslationsResponse.CategoryLanguage.IsActiveTranslationFields mapIsActiveTranslationFields(CategoriesTranslationsMapping object) {
//        CategoryTranslationsResponse.CategoryLanguage.IsActiveTranslationFields translationFields = new CategoryTranslationsResponse().new CategoryLanguage().new IsActiveTranslationFields();
//        translationFields.setTranslationKeyId(object.getIsActiveTranslationKeyId());
//        translationFields.setTranslationKey(object.getIsActiveTranslationKey());
//        translationFields.setTranslationId(object.getIsActiveTranslationId());
//        translationFields.setContent(object.getIsActiveTranslationContent());
//        return translationFields;
//    }


}


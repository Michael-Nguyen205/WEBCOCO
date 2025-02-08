package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.Models;
import spring.boot.webcococo.entities.TranslationKey;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.ModelsRepository;
import spring.boot.webcococo.repositories.TranslationKeyRepository;
import spring.boot.webcococo.services.ITranslationKeyService;

import java.lang.reflect.Field;
import java.util.Optional;

@Log4j2
@Service
public class TranslationKeyServiceImpl extends BaseServiceImpl<TranslationKey, Integer, TranslationKeyRepository> implements ITranslationKeyService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    private final TranslationKeyRepository translationKeyRepository;
    private final ModelsRepository modelsRepository;

    private final ModelsServiceImpl modelsService;
    public TranslationKeyServiceImpl(TranslationKeyRepository translationKeyRepository, TranslationKeyRepository translationKeyRepository1, ModelsRepository modelsRepository, ModelsServiceImpl modelsService) {
        super(translationKeyRepository); // Truyền repository vào lớp cha
        this.translationKeyRepository = translationKeyRepository1;
        this.modelsRepository = modelsRepository;
        this.modelsService = modelsService;
    }

    private Integer getIdEntity(Object entity) {
        try {
            // Lấy trường "id" từ class của entity
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            // Trả về giá trị của trường "id"
            return (Integer) idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AppException(ErrorCodeEnum.FIELD_NOT_FOUND, "Entity must have an ID field.");
        }
    }



    private Boolean getIsActive(Object entity) {
        try {
            // Lấy trường "id" từ class của entity
            Field idField = entity.getClass().getDeclaredField("isActive");
            idField.setAccessible(true);
            // Trả về giá trị của trường "id"
            return (Boolean) idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AppException(ErrorCodeEnum.FIELD_NOT_FOUND, "Entity must have an ID field.");
        }
    }



    @Transactional
    protected TranslationKey CreateTranslationKey(Object entity, Field fieldToTranslation) {
        try {
            // Lấy tên của entity

            // Tìm Models theo entityName
            Models models = modelsService.findOrCreateModel(entity);

            Integer entityId = getIdEntity(entity);

            // Tạo key từ thông tin model và field
            String key = models.getName() + "." + fieldToTranslation.getName()+"."+entityId;

            log.error("key:{}",key);
            // Tìm hoặc tạo mới TranslationKey


            Optional<TranslationKey> translationKey = translationKeyRepository.findByKey(key);

            if(translationKey.isPresent()){
                throw new AppException(ErrorCodeEnum.DUPLICATE_DATA,"translationKey tồn tại");
            }else {
                // tạo translation kay mới cho entity này
                TranslationKey newTranslationKey = new TranslationKey();
                newTranslationKey.setKey(key);
                newTranslationKey.setModelId(models.getId());
                // Lưu TranslationKey vào cơ sở dữ liệu
                return translationKeyRepository.save(newTranslationKey);
            }


        } catch (Exception e) {
            // Xử lý ngoại lệ
            throw e;

        }
    }
}

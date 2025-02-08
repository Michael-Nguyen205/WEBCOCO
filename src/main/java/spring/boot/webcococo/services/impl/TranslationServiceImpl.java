package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.entities.Translation;
import spring.boot.webcococo.entities.TranslationKey;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.TranslationRequest;
import spring.boot.webcococo.models.response.TranslationResponse;
import spring.boot.webcococo.repositories.I18nLanguageRepository;
import spring.boot.webcococo.repositories.TranslationKeyRepository;
import spring.boot.webcococo.repositories.TranslationRepository;
import spring.boot.webcococo.services.ITranslationService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class TranslationServiceImpl extends BaseServiceImpl<Translation, String, TranslationRepository> implements ITranslationService {
    private final TranslationKeyServiceImpl translationKeyService;

    private final I18nLanguageServiceImpl i18nLanguageService;


    private final TranslationKeyRepository translationKeyRepository;
    private final TranslationRepository translationRepository;


    private final I18nLanguageRepository i18nLanguageRepository;

    public TranslationServiceImpl(TranslationRepository translationRepository, I18nLanguageRepository i18nLanguageRepository, TranslationKeyServiceImpl translationKeyService, I18nLanguageRepository i18nLanguageRepository1, I18nLanguageServiceImpl i18nLanguageService, TranslationKeyRepository translationKeyRepository, TranslationRepository translationRepository1, I18nLanguageRepository i18nLanguageRepository2) {
        super(translationRepository); // Truyền repository vào lớp cha
        this.translationKeyService = translationKeyService;
        this.i18nLanguageService = i18nLanguageService;
        this.translationKeyRepository = translationKeyRepository;
        this.translationRepository = translationRepository1;
        this.i18nLanguageRepository = i18nLanguageRepository2;
    }


    @Transactional
    public Integer createTranslationForNewEntity(Object entity, Object request, Integer i18nLanguageId, String fieldName) {

        try {
            // Kiểm tra trường hợp không tìm thấy trường trong request
            Field fieldToTranslation = getFieldFromRequest(request.getClass(), fieldName);

            // Tìm hoặc tạo TranslationKey cho entity
            TranslationKey translationKey = translationKeyService.CreateTranslationKey(entity, fieldToTranslation);

            // Lấy ID của TranslationKey
            Integer translationKeyId = translationKey.getId();

            // Lấy thông tin ngôn ngữ
            I18Language i18Language = getI18LanguageById(i18nLanguageId);

            // Lấy giá trị của trường từ request
            Object fieldValue = getFieldValueFromRequest(request, fieldToTranslation);

            // Tạo và lưu đối tượng Translation
            Translation translation = new Translation();
            setFieldTranslation(fieldValue, translation);
            translation.setI18nLanguageId(i18Language.getId());
            translation.setTranslationKeyId(translationKeyId);
            save(translation);
            // Trả về ID của TranslationKey
            return translationKeyId;
        } catch (Exception e) {
            throw e;
        }

    }


    private I18Language getI18LanguageById(Integer i18LanguageId) {
        return i18nLanguageService.findById(i18LanguageId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND))
                ;
    }


    private void setFieldTranslation(Object fieldValue, Translation translation) {
        // Kiểm tra kiểu dữ liệu và chuyển đổi thành String
        log.error("fieldValue:{}", fieldValue);

        if (fieldValue == null) {
            throw new AppException(ErrorCodeEnum.NULL_POINTER, "khoong thay gia tri feildvalue");
        }

        translation.setContent(fieldValue.toString());
    }


    private Field getFieldFromRequest(Class<?> clazzRequest, String fieldName) {
        try {
            return clazzRequest.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new AppException(ErrorCodeEnum.FIELD_NOT_FOUND, "Field '" + fieldName + "' not found.");
        }
    }

    private Object getFieldValueFromRequest(Object request, Field fieldToTranslation) {
        if (request == null) {
            throw new IllegalArgumentException("Request object cannot be null");
        }
        if (fieldToTranslation == null) {
            throw new IllegalArgumentException("Field to translation cannot be null");
        }

        try {
            fieldToTranslation.setAccessible(true);
            return fieldToTranslation.get(request);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access the field value: " + fieldToTranslation.getName(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Field " + fieldToTranslation.getName() + " is not accessible in the given request object", e);
        }
    }





    @Override
    public List<TranslationResponse> upDateTranslation(List<TranslationRequest> translationRequests, Integer langId) {
        List<Translation> translationList = new ArrayList<>();
        translationRequests.forEach(translationRequest -> {
            I18Language i18Language = i18nLanguageRepository.findById(langId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

            TranslationKey translationKey = translationKeyRepository.findById(translationRequest.getTranslationKeyId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            Translation translation = translationRepository.findById(translationRequest.getTranslationId()).orElseThrow(()-> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

            if (!translation.getTranslationKeyId().equals(translationKey.getId()) && translation.getI18nLanguageId().equals(i18Language.getId())) {
                throw new AppException(ErrorCodeEnum.INVALID_KEY, " key của translation không khớp với trankey");
            }

            translation.setContent(translationRequest.getTranslationContent());
            translationList.add(translation);
        });
        translationRepository.saveAll(translationList);
        return null;
    }

    @Override
    public List<TranslationResponse> createTranslation(List<TranslationRequest> translationRequests, Integer langId) {
        List<Translation> translationList = new ArrayList<>();

        translationRequests.forEach(translationRequest -> {

            I18Language i18Language = i18nLanguageRepository.findById(langId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));

            TranslationKey translationKey = translationKeyRepository.findById(translationRequest.getTranslationKeyId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            Translation translation = new Translation();

            translation.setContent(translationRequest.getTranslationContent());
            translation.setTranslationKeyId(translationKey.getId() );
            translation.setI18nLanguageId(i18Language.getId());
            translationList.add(translation);
        });
        translationRepository.saveAll(translationList);
        return null;
    }

    @Override
    public String getContentFromTranslationByTranslationKeyId(Integer translationKeyId, Integer languageId) {


        TranslationKey translationKey = translationKeyRepository.findById(translationKeyId).orElseThrow(()-> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        Translation translation = translationRepository.findByTranslationKeyIdAndI18nLanguageId(translationKey.getId(),languageId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        return translation.getContent();
    }
}

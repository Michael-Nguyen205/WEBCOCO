package spring.boot.webcococo.services;

import spring.boot.webcococo.constants.Language;
import spring.boot.webcococo.entities.Translation;
import spring.boot.webcococo.entities.TranslationKey;
import spring.boot.webcococo.models.requests.TranslationRequest;
import spring.boot.webcococo.models.response.TranslationResponse;

import java.lang.reflect.Field;
import java.util.List;

public interface ITranslationService {
//     Translation createTranslation(Object entity, Object request, Integer i18nLanguageId , Field fieldRequestToTranslation)     throws NoSuchFieldException, IllegalAccessException ;


    List<TranslationResponse> upDateTranslation (List<TranslationRequest> translationRequests , Integer langId);


    List<TranslationResponse> createTranslation (List<TranslationRequest> translationRequests , Integer langId);



    String getContentFromTranslationByTranslationKeyId (Integer translationKeyId ,Integer languageId);

}

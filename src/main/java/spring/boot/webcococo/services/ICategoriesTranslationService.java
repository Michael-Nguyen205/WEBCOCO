package spring.boot.webcococo.services;

import spring.boot.webcococo.models.response.CategoryTranslationsResponse;

import java.util.List;

public interface ICategoriesTranslationService {
    List<CategoryTranslationsResponse> getAllCategoriesAndTranslationFiels ();
}

package spring.boot.webcococo.repositories;


import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.entities.Translation;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository extends BaseRepository<Translation, String> {
    // Trả về danh sách các Translation
    Optional<List<Translation>> findAllByTranslationKeyId(Integer id);
    // Trả về một Translation duy nhất
    Optional<Translation> findByTranslationKeyIdAndI18nLanguageId(Integer keyId, Integer langId);
}

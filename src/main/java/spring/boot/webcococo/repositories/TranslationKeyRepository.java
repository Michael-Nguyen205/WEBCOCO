package spring.boot.webcococo.repositories;


import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.entities.TranslationKey;

import java.util.Optional;

public interface TranslationKeyRepository extends BaseRepository<TranslationKey, Integer> {

    Optional<TranslationKey> findByKey (String key) ;


}

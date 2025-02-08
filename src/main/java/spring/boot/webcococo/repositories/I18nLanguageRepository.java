package spring.boot.webcococo.repositories;


import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.entities.I18Language;

import java.util.Optional;

public interface I18nLanguageRepository extends BaseRepository<I18Language, Integer> {

    Optional<I18Language> findByCode(String code);

}

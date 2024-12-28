package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.entities.I18Language;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.repositories.I18nLanguageRepository;
import spring.boot.webcococo.services.IBankGateWayService;
import spring.boot.webcococo.services.II18nLanguageService;


@Service
public class I18nLanguageServiceImpl extends BaseServiceImpl<I18Language, Integer, I18nLanguageRepository> implements II18nLanguageService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    public I18nLanguageServiceImpl(I18nLanguageRepository i18nLanguageRepository) {
        super(i18nLanguageRepository); // Truyền repository vào lớp cha
    }

}

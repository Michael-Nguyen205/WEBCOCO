package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Action;
import spring.boot.webcococo.repositories.ActionsRepository;
import spring.boot.webcococo.services.IActionService;


@Service
public class ActionsServiceImpl extends BaseServiceImpl<Action, Integer, ActionsRepository> implements IActionService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    public ActionsServiceImpl(ActionsRepository actionsRepository) {
        super(actionsRepository); // Truyền repository vào lớp cha
    }

}

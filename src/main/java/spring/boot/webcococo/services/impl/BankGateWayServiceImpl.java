package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.BankGateWay;
import spring.boot.webcococo.repositories.BankGateWayRepository;
import spring.boot.webcococo.services.IBankGateWayService;


@Service
public class BankGateWayServiceImpl extends BaseServiceImpl<BankGateWay, Integer, BankGateWayRepository> implements IBankGateWayService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    public BankGateWayServiceImpl(BankGateWayRepository bankGateWayRepository) {
        super(bankGateWayRepository); // Truyền repository vào lớp cha
    }

}

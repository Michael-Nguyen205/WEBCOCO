package spring.boot.webcococo.repositories;


import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.BankGatewayDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankGatewayDetailRepository extends BaseRepository<BankGatewayDetail, Integer> {

    Optional<List<BankGatewayDetail>> findAllByPaymentMethodId(Integer integer);
}

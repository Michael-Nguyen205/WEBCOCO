package spring.boot.webcococo.repositories;

import spring.boot.webcococo.entities.PaymentsConditionOnMonthly;

import java.util.List;
import java.util.Optional;

public interface PaymentConditionOnMonthlyRepository extends BaseRepository<PaymentsConditionOnMonthly, Integer> {



Optional<List<PaymentsConditionOnMonthly>> findAllByPaymentTermsId(Integer id);

}

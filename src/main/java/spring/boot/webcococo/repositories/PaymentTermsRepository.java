package spring.boot.webcococo.repositories;

import spring.boot.webcococo.entities.PaymentTerms;

import java.util.Optional;

public interface PaymentTermsRepository extends BaseRepository<PaymentTerms, Integer> {

    Optional<PaymentTerms> findByPackagesId(Integer is);

}

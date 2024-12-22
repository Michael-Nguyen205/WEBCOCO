package spring.boot.webcococo.repositories;


import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.boot.webcococo.entities.PaymentMethod;

import java.util.List;

public interface PaymentMethodRepository extends BaseRepository<PaymentMethod, Integer> {

    @Query(value =
            "WITH RECURSIVE category_tree AS (" +
                    "    SELECT id, name, parent_id, 1 AS level " +
                    "    FROM categories " +
                    "    WHERE id = :rootId " +
                    "    UNION ALL " +
                    "    SELECT c.id, c.name, c.parent_id, ct.level + 1 AS level " +
                    "    FROM categories c " +
                    "    INNER JOIN category_tree ct ON c.parent_id = ct.id" +
                    ") " +
                    "SELECT id, name, parent_id, level FROM category_tree " +
                    "ORDER BY level, id", nativeQuery = true)
    List<Tuple> findPaymentMethod(@Param("rootId") Integer rootId);


}

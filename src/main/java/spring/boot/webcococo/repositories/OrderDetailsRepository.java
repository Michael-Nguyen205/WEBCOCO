package spring.boot.webcococo.repositories;

import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.Models;
import spring.boot.webcococo.entities.OrderDetails;

@Repository
public interface OrderDetailsRepository extends  BaseRepository<OrderDetails, Integer>{
}

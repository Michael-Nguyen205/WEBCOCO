package spring.boot.webcococo.repositories;


import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.Models;

import java.util.Optional;


@Repository
public interface ModelsRepository extends BaseRepository<Models, Integer> {

    Optional<Models> findByName(String name);

}

package spring.boot.webcococo.repositories;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.Categories;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Categories,Integer> {

     Optional<List<Categories>> findAllByParentId (Integer integer);

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
    List<Tuple> findCategoryTree(@Param("rootId") Integer rootId);








}










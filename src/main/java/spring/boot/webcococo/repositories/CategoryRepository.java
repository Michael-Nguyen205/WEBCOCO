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
            "WITH RECURSIVE category_tree AS (\n" +
                    "    SELECT \n" +
                    "        c.id, \n" +
                    "        c.name, \n" +
                    "        c.parent_id, \n" +
                    "        c.i18n_language_id,\n" +
                    "         c.is_active,\n" +
                    "        l.code,\n" +
                    "        1 AS level\n" +
                    "    FROM \n" +
                    "        categories c\n" +
                    "    LEFT JOIN \n" +
                    "        i18n_language l\n" +
                    "    ON \n" +
                    "        c.i18n_language_id = l.id\n" +
                    "    WHERE \n" +
                    "        c.id = :rootId /* Sử dụng id gốc là 1 */\n" +
                    "    UNION ALL\n" +
                    "    SELECT \n" +
                    "        c.id, \n" +
                    "        c.name, \n" +
                    "        c.parent_id, \n" +
                    "        c.i18n_language_id,\n" +
                    "         c.is_active,\n" +
                    "        l.code,\n" +
                    "        ct.level + 1 AS level\n" +
                    "    FROM \n" +
                    "        categories c\n" +
                    "    LEFT JOIN \n" +
                    "        i18n_language l\n" +
                    "    ON \n" +
                    "        c.i18n_language_id = l.id\n" +
                    "    INNER JOIN \n" +
                    "        category_tree ct \n" +
                    "    ON \n" +
                    "        c.parent_id = ct.id\n" +
                    ") \n" +
                    "SELECT \n" +
                    "    id, \n" +
                    "    name, \n" +
                    "    parent_id, \n" +
                    "    i18n_language_id,\n" +
                    "    code AS language_code ,\n" +
                    "    is_active,\n" +
                    "    level\n" +
                    "FROM \n" +
                    "    category_tree\n" +
                    "ORDER BY \n" +
                    "    level, \n" +
                    "    id;", nativeQuery = true)
    List<Tuple> findCategoryTree(@Param("rootId") Integer rootId);





    @Query(value =
            "     SELECT \n" +
                    "        c.id, \n" +
                    "        c.name, \n" +
                    "        c.parent_id, \n" +
                    "        c.i18n_language_id,\n" +
                    "        l.code AS language_code ,\n" +
                    "        c.is_active,\n" +
                    "        1 AS level\n" +
                    "    FROM \n" +
                    "        categories c\n" +
                    "            LEFT JOIN \n" +
                    "        i18n_language l\n" +
                    "    ON \n" +
                    "        c.i18n_language_id = l.id; ", nativeQuery = true)
    List<Tuple> getAllCategoryTree( );


}










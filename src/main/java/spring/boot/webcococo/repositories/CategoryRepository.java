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
                    "        c.id AS c_id,\n" +
                    "        c.parent_id AS c_parent_id,\n" +
                    "        t_name.content AS c_name,\n" +
                    "        t_is_active.content AS c_is_active,\n" +
                    "        inl.id AS l_id,\n" +
                    "        inl.code AS l_code,\n" +
                    "        tk_n.id AS name_translation_key_id,\n" +
                    "        tk_ia.id AS is_active_translation_key_id,\n" +
                    "        tk_n.key AS name_translation_key,\n" +
                    "        tk_ia.key AS is_active_translation_key,\n" +
                    "        1 AS level\n" +
                    "    FROM \n" +
                    "        categories c\n" +
                    "    LEFT JOIN translation_key tk_n \n" +
                    "        ON c.name_translation_key_id = tk_n.id  \n" +
                    "    LEFT JOIN translation_key tk_ia \n" +
                    "        ON c.is_active_translation_key_id = tk_ia.id \n" +
                    "    LEFT JOIN translation t_name \n" +
                    "        ON t_name.translation_key_id = tk_n.id\n" +
                    "    LEFT JOIN translation t_is_active \n" +
                    "        ON t_is_active.translation_key_id = tk_ia.id\n" +
                    "     JOIN i18n_language inl \n" +
                    "        ON inl.id = t_name.i18n_language_id \n" +
                    "        AND inl.id = t_is_active.i18n_language_id  \n" +
                    "        AND inl.code = :langCode \n" +
                    "    WHERE \n" +
                    "        c.id = :rootId \n" +
                    "    UNION ALL\n" +
                    "    SELECT \n" +
                    "        c.id AS c_id,\n" +
                    "        c.parent_id AS c_parent_id,\n" +
                    "        t_name.content AS c_name,\n" +
                    "        t_is_active.content AS c_is_active,\n" +
                    "        inl.id AS l_id,\n" +
                    "        inl.code AS l_code,\n" +
                    "        tk_n.id AS name_translation_key_id,\n" +
                    "        tk_ia.id AS is_active_translation_key_id,\n" +
                    "        tk_n.key AS name_translation_key,\n" +
                    "        tk_ia.key AS is_active_translation_key,\n" +
                    "        ct.level + 1 AS level\n" +
                    "    FROM \n" +
                    "        categories c\n" +
                    "    LEFT JOIN translation_key tk_n \n" +
                    "        ON c.name_translation_key_id = tk_n.id  \n" +
                    "    LEFT JOIN translation_key tk_ia \n" +
                    "        ON c.is_active_translation_key_id = tk_ia.id \n" +
                    "    LEFT JOIN translation t_name \n" +
                    "        ON t_name.translation_key_id = tk_n.id\n" +
                    "    LEFT JOIN translation t_is_active \n" +
                    "        ON t_is_active.translation_key_id = tk_ia.id\n" +
                    "     JOIN i18n_language inl \n" +
                    "        ON inl.id = t_name.i18n_language_id \n" +
                    "        AND inl.id = t_is_active.i18n_language_id  \n" +
                    "        AND inl.code = :langCode\n" +
                    "    INNER JOIN category_tree ct \n" +
                    "        ON c.parent_id = ct.c_id\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    c_id,\n" +
                    "    c_parent_id,\n" +
                    "    c_name,\n" +
                    "    c_is_active,\n" +
                    "    l_id,\n" +
                    "    l_code,\n" +
                    "    name_translation_key_id,\n" +
                    "    is_active_translation_key_id,\n" +
                    "    name_translation_key,\n" +
                    "    is_active_translation_key,\n" +
                    "    level\n" +
                    "FROM \n" +
                    "    category_tree\n" +
                    "ORDER BY \n" +
                    "    level, \n" +
                    "    c_id;", nativeQuery = true)
    List<Tuple> findCategoryTree(@Param("rootId") Integer rootId ,@Param("langCode") String langCode);





    @Query(value =
            "       SELECT \n" +
                    " c.id AS c_id,\n" +
                    " c.parent_id AS  c_parent_id,\n" +
                    " t_name.content AS c_name,\n" +
                    " t_is_active.content AS c_is_active,\n" +
                    " inl.id AS l_id,\n" +
                    " inl.code AS l_code,\n" +
                    " tk_n.id AS name_translation_key_id,\n" +
                    " tk_ia.id is_active_translation_key_id,\n" +
                    " tk_n.key AS name_translation_key ,\n" +
                    " tk_ia.key  AS is_active_translation_key\n" +
                    "   FROM \n" +
                    "    categories c\n" +
                    "LEFT JOIN translation_key tk_n ON c.name_translation_key_id = tk_n.id  \n" +
                    "LEFT JOIN translation_key tk_ia ON c.is_active_translation_key_id  = tk_ia.id \n" +
                    "LEFT JOIN translation t_name ON t_name.translation_key_id = tk_n.id\n" +
                    "LEFT JOIN translation t_is_active ON t_is_active.translation_key_id = tk_ia.id\n" +
                    "LEFT JOIN i18n_language inl ON inl.id = t_name.i18n_language_id AND inl.id = t_is_active.i18n_language_id  WHERE inl.code = :languageCode; ", nativeQuery = true)
    List<Tuple> getAllCategoryTreeByLanguage( @Param("languageCode") String languageCode );










//
//    @Query(value =
//            " SELECT \n" +
//                    "    c.id AS c_id,\n" +
//                    "    c.parent_id AS c_parent_id,\n" +
//                    "    inl.id AS l_id,\n" +
//                    "    inl.code AS l_code,\n" +
//                    "    tk.id AS tk_id,\n" +
//                    "    tk.key AS tk_key,\n" +
//                    "    CASE \n" +
//                    "        WHEN tk.id = c.name_translation_key_id THEN 'name_translation_key'\n" +
//                    "        WHEN tk.id = c.is_active_translation_key_id THEN 'is_active_translation_key'\n" +
//                    "    END AS translation_type,\n" +
//                    "    t.id AS t_id,\n" +
//                    "    t.content AS t_content,\n" +
//                    "    t.translation_key_id AS t_translation_key_id\n" +
//                    "FROM \n" +
//                    "    categories c\n" +
//                    "LEFT JOIN translation_key tk \n" +
//                    "    ON tk.id = c.name_translation_key_id \n" +
//                    "    OR tk.id = c.is_active_translation_key_id\n" +
//                    "LEFT JOIN translation t \n" +
//                    "    ON t.translation_key_id = tk.id\n" +
//                    "LEFT JOIN i18n_language inl \n" +
//                    "    ON inl.id = t.i18n_language_id\n" +
//                    "    ORDER BY  c.id,inl.code ;\n" +
//                    "; ", nativeQuery = true)
//    List<Tuple> getAllCategoriesWithTranslations( );
//
//
//
//





    @Query(value =
            "     SELECT\n" +
                    "    c.id AS c_id,\n" +
                    "    c.parent_id AS c_parent_id,\n" +
                    "    inl.id AS l_id,\n" +
                    "    inl.code AS l_code,\n" +
                    "    tk_name.id AS tk_name_id,\n" +
                    "    tk_name.key AS tk_name_key,\n" +
                    "    t_name.id AS t_name_id,\n" +
                    "    t_name.content AS t_name_content,\n" +
                    "    t_name.translation_key_id AS t_name_translation_key_id,\n" +
                    "    tk_is_active.id AS tk_is_active_id,\n" +
                    "    tk_is_active.key AS tk_is_active_key,\n" +
                    "    t_is_active.id AS t_is_active_id,\n" +
                    "    t_is_active.content AS t_is_active_content,\n" +
                    "    t_is_active.translation_key_id AS t_is_active_translation_key_id\n" +
                    "            FROM\n" +
                    "    categories c\n" +
                    "    LEFT JOIN translation_key tk_name\n" +
                    "    ON tk_name.id = c.name_translation_key_id\n" +
                    "    LEFT JOIN translation_key tk_is_active\n" +
                    "    ON tk_is_active.id = c.is_active_translation_key_id\n" +
                    "    LEFT JOIN translation t_name\n" +
                    "    ON t_name.translation_key_id = tk_name.id\n" +
                    "    LEFT JOIN translation t_is_active\n" +
                    "    ON t_is_active.translation_key_id = tk_is_active.id\n" +
                    "    JOIN i18n_language inl\n" +
                    "    ON inl.id = t_name.i18n_language_id\n" +
                    "    AND inl.id = t_is_active.i18n_language_id\n" +
                    "    ; ", nativeQuery = true)
    List<Tuple> getAllCategoriesWithTranslations( );









}










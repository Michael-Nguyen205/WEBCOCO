package spring.boot.webcococo.repositories;


import spring.boot.webcococo.entities.Permission;

import java.util.List;

public interface PermissionRepository extends BaseRepository<Permission, Integer> {
    List<Permission> findByIdIn(List<Integer> id);









//    @Query(value = "SET @permission_id = ?1; " +
//            "SET @sql = ''; " +
//            "SELECT GROUP_CONCAT(CONCAT('DELETE FROM ', table_name, ' WHERE permission_id = ', @permission_id, ';') SEPARATOR ' ') INTO @sql " +
//            "FROM information_schema.columns " +
//            "WHERE column_name = 'permission_id' " +
//            "AND table_schema = 'webcococo'; " +
//            "PREPARE stmt FROM @sql; " +
//            "EXECUTE stmt; " +
//            "DEALLOCATE PREPARE stmt;")
//    Mono<Void> deleteByPermissionId(Integer permissionId);





//
//
//    DELIMITER //
//    SET @permission_id = 5;
//    SET @sql = '';
//    SELECT
//    GROUP_CONCAT(CONCAT('DELETE FROM ', table_name, ' WHERE permission_id = ', @permission_id) SEPARATOR '; ') INTO @sql
//    FROM information_schema.columns
//    WHERE column_name = 'permission_id'
//    AND table_schema = 'webcococo';
//    PREPARE stmt FROM @sql;
//    EXECUTE stmt;
//    DEALLOCATE PREPARE stmt;
////
//    DELIMITER ;
//
//








}

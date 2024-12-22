package spring.boot.webcococo.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntityCascadeDeletionUtil {

    private final JdbcTemplate jdbcTemplate;

    public void deleteByParentId(Integer parentId, String parentColumnName) {
        try {
            String findTablesQuery = "SELECT table_name FROM information_schema.columns "
                    + "WHERE column_name = ? AND table_schema = 'webcococo'";
            var tableNames = jdbcTemplate.queryForList(findTablesQuery, String.class, parentColumnName);

            for (String tableName : tableNames) {
                String deleteQuery = "DELETE FROM " + tableName + " WHERE " + parentColumnName + " = ?";
                int rowsAffected = jdbcTemplate.update(deleteQuery, parentId);
                log.info("Deleted {} rows from table {} where {} = {}", rowsAffected, tableName, parentColumnName, parentId);
            }
        } catch (Exception e) {
            log.error("Error deleting by parent ID: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DATABASE_DELETE_ERROR, "Error during cascade deletion");
        }
    }

    public void deleteByEntityParentIdOnTable(Integer permissionId, String tableName) {
        if (permissionId == null) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Permission ID not found");
        }
        try {
            String query = "DELETE FROM " + tableName + " WHERE permission_id = ?";
            int rowsAffected = jdbcTemplate.update(query, permissionId);

            if (rowsAffected > 0) {
                log.info("Deleted {} rows from table {} where permission_id = {}", rowsAffected, tableName, permissionId);
            } else {
                throw new AppException(ErrorCodeEnum.DATABASE_DELETE_ERROR, "No rows found to delete");
            }
        } catch (Exception e) {
            log.error("Error deleting by entity parent ID: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DATABASE_DELETE_ERROR, "Error during deletion by entity parent ID");
        }
    }
}

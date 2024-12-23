package spring.boot.webcococo.models.mapping;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Data // Lombok annotation to generate getters, setters, toString, etc.
public class PermissionModelActionRawMapping {

    static AtomicInteger loopCreatePackagepProjectionMapping = new AtomicInteger(0);

    // Permission fields
    private Integer permissionId;
    private String permissionName;

    // Model fields
    private Integer modelId;
    private String modelName;
    private String modelDescription;

    // Action fields
    private Integer actionId;
    private String actionName;
    private String actionDescription;


    // Raw fields
    private Integer rawId;


    public static Set<PermissionModelActionRawMapping> toPermissionModelActionMapping(ResultSet rs) throws SQLException {
        Set<PermissionModelActionRawMapping> permissionModelActionMappings = new HashSet<>();
        while (rs.next()) {

            PermissionModelActionRawMapping permissionModelActionRawMapping = new PermissionModelActionRawMapping();

            // Mapping Permission fields
            permissionModelActionRawMapping.setPermissionId(rs.getInt("permission_id"));
            permissionModelActionRawMapping.setPermissionName(rs.getString("permission_name"));

            // Mapping Model fields
            permissionModelActionRawMapping.setModelId(rs.getInt("model_id"));
            permissionModelActionRawMapping.setModelName(rs.getString("model_name"));
            permissionModelActionRawMapping.setModelDescription(rs.getString("model_description"));

            // Mapping Action fields
            permissionModelActionRawMapping.setActionId(rs.getInt("action_id"));
            permissionModelActionRawMapping.setActionName(rs.getString("action_name"));
            permissionModelActionRawMapping.setActionDescription(rs.getString("action_description"));


            // Mapping Raw fields
            permissionModelActionRawMapping.setRawId(rs.getInt("raw_id"));

            // Add the mapped object to the set
            permissionModelActionMappings.add(permissionModelActionRawMapping);
        }


        return permissionModelActionMappings;
    }
}

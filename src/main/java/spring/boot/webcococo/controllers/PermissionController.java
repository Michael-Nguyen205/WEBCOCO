package spring.boot.webcococo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.Permission;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.PermissionResponse;
import spring.boot.webcococo.repositories.PermissionRepository;
import spring.boot.webcococo.services.impl.PermissionServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/permission")
public class PermissionController {

    private final ModelMapper modelMapper;
    private final PermissionServiceImpl permissionService;
    private final PermissionRepository permissionRepository;
    private final EntityCascadeDeletionUtil deletionUtil;

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable Integer id) {
        log.error("Đã vào đây actions controller");

        // Tìm kiếm Permission bằng ID
        Permission permission = permissionRepository.findById(id).orElse(null);

        // Kiểm tra nếu không tìm thấy Permission
        if (permission == null) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy permission");
        }

        // Trả về Permission nếu tìm thấy
        PermissionResponse response = new PermissionResponse(permission.getId(), permission.getName(), null);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePermission(@PathVariable Integer id) {
        log.error("Đã vào trong deletePermission");

        try {
            permissionService.deleteById(id);
            deletionUtil.deleteByParentId(id, "permission_id");
            return ResponseEntity.ok(new ApiResponse(null, "Permission deleted successfully", null, null));
        } catch (AppException e) {
            log.error("Lỗi khi xóa permission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Error deleting permission", null, null));
        } catch (Exception e) {
            log.error("Lỗi không xác định khi xóa permission", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Error deleting permission", null, null));
        }
    }



    @GetMapping("")
    @PreAuthorize("@authorUtils.hasAuthor('VIEW','PERMISSION',null)")
    public ResponseEntity<List<PermissionResponse>> getAllPermission() {
        try {
            List<Permission> permissions = permissionService.findAll();

            List<PermissionResponse> permissionResponses =  permissions.stream()
                    .map(permission ->
                new PermissionResponse(permission.getId(),permission.getName(),null))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(permissionResponses);
        } catch (AppException e) {
            log.error("Lỗi khi lấy tất cả permissions", e);
            throw e;
        } catch (Exception e) {
               throw e;
        }
    }

}

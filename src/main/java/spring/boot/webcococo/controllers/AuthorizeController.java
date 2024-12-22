package spring.boot.webcococo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.AddAuthorForUserRequest;
import spring.boot.webcococo.models.requests.CreatePermissionRequest;
import spring.boot.webcococo.models.requests.RemoveAuthorForUserRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.AuthorizeResponse;
import spring.boot.webcococo.models.response.UserPermissionResponse;
import spring.boot.webcococo.repositories.PermissionRepository;
import spring.boot.webcococo.services.impl.AuthorizeServiceImpl;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/authorize")
@RequiredArgsConstructor
public class AuthorizeController {

    private final AuthorizeServiceImpl permissionService;
    private final PermissionRepository permissionRepository;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthorizeResponse> updateAuthor(@RequestBody @Valid CreatePermissionRequest request, @PathVariable Integer id) {
        log.error("da vao updateAuthor");
        return permissionRepository.findById(id)
                .map(existingPermission -> permissionService.updateAuthor(request, id))
                .map(updatedPermission -> new ResponseEntity<>(updatedPermission, HttpStatus.OK))
                .orElseThrow(() -> new AppException(ErrorCodeEnum.INVALID_KEY.DATA_NOT_FOUND, "khong thay permission"));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorizeResponse> createAuthor(@RequestBody @Valid CreatePermissionRequest request) {
        log.error("da vao day");
        try {
            AuthorizeResponse response = permissionService.createAuthor(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AppException e) {
            throw e; // Rethrow custom exception
        }
    }

    @PostMapping("/addAuthorForUser")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserPermissionResponse> addAuthorForUser(@RequestBody @Valid AddAuthorForUserRequest request) {
        log.error("da vao day");
        try {
            UserPermissionResponse response = permissionService.addAuthor(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AppException e) {
            throw e; // Rethrow custom exception
        }
    }

    @DeleteMapping("/removeAuthorForUser")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> removeAuthorForUser(@RequestBody @Valid RemoveAuthorForUserRequest request) {
        log.error("da vao day");
        try {
            UserPermissionResponse userPermissionResponse = permissionService.removeAuthorForUser(request);
            return ResponseEntity.ok(new ApiResponse(null, "removeAuthorForUser successfully", null, userPermissionResponse));
        } catch (AppException e) {
            throw e; // Rethrow custom exception
        }
    }
}

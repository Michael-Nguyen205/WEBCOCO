package spring.boot.webcococo.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.PermissionActionModelRawRepository;

@RequiredArgsConstructor
@Log4j2
@Component
public class AuthorUtils {
    private final PermissionActionModelRawRepository permissionActionRawRepository;

    public boolean hasAuthor(String action, String model, Integer rawId) {
        log.error("hasauthorr");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.error("Authentication is null");
            throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "Authentication không hợp lệ");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            log.error("Principal is not an instance of UserDetails");
            throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "Principal không hợp lệ");
        }

        String username = userDetails.getUsername();
        if (username == null) {
            log.error("Username is null in UserDetails");
            throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "Username không hợp lệ");
        }

        try {
            if (model == null && action == null) {
                return false;
            } else if (model != null && action == null && rawId == null) {
                return permissionActionRawRepository.countActionModelRawAuthors(username, rawId, null, model) > 0;
            } else if (model != null && action != null && rawId == null) {
                return permissionActionRawRepository.countActionModelRawAuthors(username, action, model) > 0;
            } else {
                return permissionActionRawRepository.countActionModelRawAuthors(username, rawId, action, model) > 0;
            }
        } catch (DataAccessException e) {
            log.error("Database error while checking permissions: ", e);
            throw new RuntimeException("An error occurred while checking permissions.", e);
        } catch (Exception e) {
            log.error("Unexpected error while checking permissions: ", e);
            throw new RuntimeException("An unexpected error occurred while checking permissions.", e);
        }
    }
}

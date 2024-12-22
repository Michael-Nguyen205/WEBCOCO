package spring.boot.webcococo.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeEnum {
    INVALID_TOKEN(1000, "error.invalid_token", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "error.user_existed", HttpStatus.BAD_REQUEST),
    DATABASE_DELETE_ERROR(1003, "error.sql_error", HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA(1006, "error.duplicate_data", HttpStatus.CONFLICT),
    UNCATEGORIZED_EXCEPTION(9999, "error.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "error.invalid_key", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "error.username_invalid", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "error.invalid_password", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "error.user_not_existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "error.unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "error.unauthorized", HttpStatus.FORBIDDEN),
    DATA_EXISTED(1011, "error.data_existed", HttpStatus.FORBIDDEN),
    OUT_OF_STOCK(1009, "error.out_of_stock", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND(400, "error.data_not_found", HttpStatus.BAD_REQUEST),
    ERROR_GENERATE_TOKEN(402, "error.error_generate_token", HttpStatus.BAD_REQUEST),
    DATABASE_SAVE_ERROR(2001, "error.database_save_error", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOAD_IMAGES_FILE_LARGE(2003,"error.upload_images_large",HttpStatus.BAD_REQUEST),
    CONTENT_TYPE_NOT_MATCH(1004, "error.content_type_not_match", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1005, "error.file_upload_failed", HttpStatus.BAD_REQUEST),
    NULL_POINTER(1005, "error.null_pointer", HttpStatus.BAD_REQUEST),
    INVALID_OPERATION(1008,"error.invalid_operation", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1009,"error.canot_send_email", HttpStatus.BAD_REQUEST),
    JSON_MAP_ERRO(1000, "error.json_map_error", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCodeEnum(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

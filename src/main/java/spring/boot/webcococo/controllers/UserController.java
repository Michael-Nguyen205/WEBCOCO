package spring.boot.webcococo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.UserLoginRequest;
import spring.boot.webcococo.models.requests.UserRegisterRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.UserLoginResponse;
import spring.boot.webcococo.models.response.UserRegisterResponse;
import spring.boot.webcococo.services.impl.UserServiceImpl;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
@Log4j2
public class UserController {

    //    private final IUserService userService;
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {


        log.error("đã vào register");
        if (!userRegisterRequest.getRetypePassword().equals(userRegisterRequest.getPassword())) {
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "Password nhập lại không khớp");
        }
        try {
            UserRegisterResponse result = userService.createUser(userRegisterRequest);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (AppException e) {
            // Xử lý ngoại lệ và trả về mã lỗi thích hợp
            throw e;
        }
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                                   HttpServletRequest request) {
        try {
            UserLoginResponse result = userService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword(), request);
            log.error("ddas cos result");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (AppException e) {
            // Xử lý ngoại lệ bằng cách ném lại để Spring Boot xử lý
            throw e;
        } catch ( Exception e){
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED,"tai khoan hoawc mat khau sai");
        }

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteUser(@PathVariable String id) {

        try {
            log.error("Đã vào trong delete actions");
            userService.deleteUser(id);
            return new ApiResponse(null, "Users deleted successfully", null, null);
        } catch (AppException e) {
            throw e;
        }
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getUser(@PathVariable String id) {
        try {
            return new ApiResponse(null, "Users deleted successfully", null, userService.getUser(id));
        } catch (AppException e) {
            throw e;
        }
    }



    @PreAuthorize("@authorUtils.hasAuthor('UPDATE','USER',null)")
    @PutMapping(value = "/approve/{statusActive}/{userId}")
    public ApiResponse<?> approveOrder(@PathVariable Boolean statusActive ,@PathVariable String userId,HttpServletRequest request) {
        try {

            userService.approveUser(statusActive,userId );
            log.error("đa vao controller");
            return ApiResponse.builder().code(200).detailMess("rgr").result("aprrove usser thanh cong ").build();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }




}

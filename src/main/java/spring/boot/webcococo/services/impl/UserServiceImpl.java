package spring.boot.webcococo.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.UserPermission;
import spring.boot.webcococo.entities.Users;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CustomUserDetails;
import spring.boot.webcococo.models.requests.UserRegisterRequest;
import spring.boot.webcococo.models.response.UserLoginResponse;
import spring.boot.webcococo.models.response.UserRegisterResponse;
import spring.boot.webcococo.models.response.UserResponse;
import spring.boot.webcococo.repositories.PermissionRepository;
import spring.boot.webcococo.repositories.UserRepository;
import spring.boot.webcococo.repositories.UsersPermissionRepository;
import spring.boot.webcococo.services.ITokenService;
import spring.boot.webcococo.services.IUserService;
import spring.boot.webcococo.utils.JwtTokenUtil;

import java.util.List;

@Log4j2

@Service("userServiceImpl")  // Đặt tên cho bean
public class UserServiceImpl implements IUserService {

    //extends BaseServiceImpl<Users, Integer, UserRepository>
    private final UserRepository userRepository;
    private final UsersPermissionRepository usersPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final ITokenService tokenService;
    private final PermissionRepository permissionRepository;

    UserServiceImpl(UserRepository repo, UserRepository userRepository, UsersPermissionRepository usersPermissionRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, ITokenService tokenService, PermissionRepository permissionRepository) {
//        super(repo);
        this.userRepository = userRepository;
        this.usersPermissionRepository = usersPermissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenService = tokenService;
        this.permissionRepository = permissionRepository;
    }


    @Transactional
    @Override
    public UserRegisterResponse createUser(UserRegisterRequest userRegisterRequest) {


        try {
            log.error("email:{}",userRegisterRequest.getEmail());
            // Kiểm tra sự tồn tại của email
            if (userRegisterRequest.getEmail() != null) {
                validateEmailNotExist(userRegisterRequest.getEmail());
            } else {
                throw new AppException(ErrorCodeEnum.NULL_POINTER, "email khong tim thay");
            }




            // Tạo user mới
            Users newUser = Users.builder()
                    .name(userRegisterRequest.getName())
                    .phoneNumber(userRegisterRequest.getPhoneNumber())
                    .email(userRegisterRequest.getEmail())
                    .isActive(true)
                    .build();


            //kiểm tra quyền
            if (userRegisterRequest.getPermissionId() != null) {
                newUser.setIsActive(false);
                // nếu yêu cầu đăng ký có quyền thì phải validate và chưa active vội
                validatePermissionNotExist(userRegisterRequest.getPermissionId());
            }


            // Mã hóa và set password cho user
            newUser.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            // Lưu người dùng vào database
            Users savedUser = userRepository.save(newUser);

            // Tạo và lưu quyền cho người dùng
            createUserPermission(savedUser.getId(), userRegisterRequest.getPermissionId());

            // Trả về thông tin người dùng đã đăng ký
            return buildUserRegisterResponse(savedUser);
        } catch (Exception e) {
            throw e;
        }
    }

    // Kiểm tra sự tồn tại của email
    private void validateEmailNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED, "Email already exists");
        }
    }

    private void validatePermissionNotExist(Integer permissionId) {
        permissionRepository.findById(permissionId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "khoong thayss quyen nay"));
    }


    // Tạo quyền cho người dùng
    private void createUserPermission(String userId, Integer permissionId) {
        UserPermission userPermissions = new UserPermission();
        userPermissions.setUserId(userId);
        userPermissions.setPermissionId(permissionId != null ? permissionId : 1);  // Default to permission 1
        usersPermissionRepository.save(userPermissions);
    }

    // Xây dựng response sau khi người dùng được đăng ký
    private UserRegisterResponse buildUserRegisterResponse(Users savedUser) {
        return UserRegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .username(savedUser.getUsername())
                .phoneNumber(savedUser.getPhoneNumber())
                .email(savedUser.getEmail())
                .build();
    }


    @Transactional
    @Override
    public UserLoginResponse login(String email, String password, HttpServletRequest request) {

        try {

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Email or password cannot be null or empty");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            log.info("Authentication token created: {}", authenticationToken);
            var authentication = authenticationManager.authenticate(authenticationToken);


            List<String> permissionList = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();


            log.error("authentication.getPrincipal(): {}", authentication.getPrincipal());
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Users users = customUserDetails.getUser();


            validateIsUserActive(users);


            // Tạo token JWT
            String token = jwtTokenUtil.generateToken(authentication);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            boolean isMobile = isMobileDevice(userAgent);

            tokenService.addToken(email, token, isMobile);

            return UserLoginResponse.builder()
                    .user(UserResponse.toUserResponse(users))
                    .token(token)
                    .permissionList(permissionList)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Login error: {}", e.getMessage());
            throw e;
        }
    }


    private void validateIsUserActive(Users user) {
        if (user.getIsActive() == false) {
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "loi dang nhap");
        }
    }

    @Override
    public void deleteUser(String userId) {

        try {
            Users users = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            users.setIsActive(false);
        } catch (AppException e) {
            throw e;
        }

    }

    @Override
    public void approveUser(Boolean statusActive, String userId) {
        try {
            Users users = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            users.setIsActive(statusActive);
        } catch (AppException e) {
            throw e;
        }
    }


    @Override
    public Users getUser(String userId) {

        try {
            Users users = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            return users;
        } catch (AppException e) {
            throw e;
        }

    }


    private boolean isMobileDevice(String userAgent) {
        return userAgent != null && userAgent.toLowerCase().contains("mobile");
    }
}

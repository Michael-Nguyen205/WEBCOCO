package spring.boot.webcococo.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.pojos.CustomUserDetails;
import spring.boot.webcococo.models.requests.*;
import spring.boot.webcococo.models.response.*;
import spring.boot.webcococo.repositories.*;
import spring.boot.webcococo.services.IAuthorizeService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthorizeServiceImpl implements IAuthorizeService {

    private final PermissionRepository permissionRepository;
    //    private final Author permissionRepository;
    private final ActionsRepository actionsRepository;
    private final PermissionActionModelRawRepository permissionActionModelRawRepository;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final UserRepository userRepository;
    private  final  ModelsRepository modelsRepository;
    private final UsersPermissionRepository usersPermissionRepository;

    @Override
    public UserPermissionResponse addAuthor(AddAuthorForUserRequest permissionRequest) {
        String emailUser = permissionRequest.getUserEmail();
        Integer permissionId = permissionRequest.getPermissionId();

        // Tìm người dùng theo email
        Users user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy user"));

        // Kiểm tra quyền đã tồn tại cho người dùng
        if (usersPermissionRepository.findByUserIdAndPermissionId(user.getId(), permissionId).isPresent()) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED,"DA TON TAI QUYEN NAY VOI NGUOI NAY");
        }

        // Nếu không có quyền, tạo quyền mới cho người dùng
        UserPermission newUserPermission = new UserPermission();
        newUserPermission.setId(UUID.randomUUID().toString());
        newUserPermission.setUserId(user.getId());
        newUserPermission.setPermissionId(permissionId);

        // Lưu quyền mới
        usersPermissionRepository.save(newUserPermission);
        return UserPermissionResponse.builder()
                .userId(user.getId())
                .permissionId(permissionId)
                .build();
    }

    @Override
    public UserPermissionResponse removeAuthorForUser(RemoveAuthorForUserRequest permissionRequest) {
        String emailUser = permissionRequest.getUserEmail();
        Integer permissionId = permissionRequest.getPermissionId();

        // Tìm người dùng theo email
        Users user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy user"));

        // Kiểm tra xem quyền có tồn tại không
        UserPermission existingUserPermission = usersPermissionRepository.findByUserIdAndPermissionId(user.getId(), permissionId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "không tìm thấy quyền để xóa"));

        // Xóa quyền của người dùng
        usersPermissionRepository.deleteUserPermissionByUserIdAndPermissionId(existingUserPermission.getUserId(), existingUserPermission.getPermissionId());
        return (UserPermissionResponse.builder()
                .userId(user.getId())
                .permissionId(permissionId)
                .build());
    }









    @Override
    public AuthorizeResponse createAuthor(CreatePermissionRequest permissionRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                log.error("Không tìm thấy thông tin xác thực");
                throw new RuntimeException("Người dùng chưa được xác thực");
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                userDetails.getAuthorities().forEach(authority ->
                        log.info("Quyền: {}", authority.getAuthority())
                );
                boolean isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(permission -> "PERMISSION_ADMIN".equals(permission.getAuthority())); // Kiểm tra quyền "ADMIN"

                if (isAdmin) {
                    log.info("User {} has ADMIN permission", userDetails.getUsername());
                } else {
                    throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "USER khong cos quuyen taoj role");
                }
                log.info("Authenticated user: {}", userDetails.getUsername());
            } else {
                log.warn("Principal is not an instance of CustomUserDetails. Principal: {}", principal);
            }







            log.error("đã vao trong createAuthor");
            Integer permissionId = permissionRequest.getId() == null ? 0 : permissionRequest.getId();

            // Tạo hoặc cập nhật quyền
            Permission savedPermission = createOrUpdatePermission(permissionRequest, permissionId);

            // Xử lý các yêu cầu liên quan đến actions
            List<PermissionActionModelRaw> permissionActionModelRawList = new ArrayList<>();
            List<ModelResponse> modelResponseList = processModelRequest(permissionActionModelRawList, permissionId, permissionRequest);

            log.error("modelResponseList:{}", modelResponseList);
            AuthorizeResponse authorizeResponse = createAuthorizeResponse(savedPermission, modelResponseList);


            permissionActionModelRawList.forEach(permissionModelActionRaw -> {
                if (permissionModelActionRaw.getId() == null) {
                    throw new IllegalStateException("ID must not be null before saving");
                }
            });

            // Lưu vào cơ sở dữ liệu
            permissionActionModelRawRepository.saveAll(permissionActionModelRawList);

            return authorizeResponse;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }










    @Override
    public AuthorizeResponse updateAuthor(CreatePermissionRequest permissionRequest, Integer permissionId) {
        if (permissionId == null) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
        // Tạo hoặc cập nhật quyền
        Permission savedPermission = createOrUpdatePermission(permissionRequest, permissionId);

        // Xóa quyền cũ trước khi cập nhật
        clearPermissionActionsForUpdate(permissionId);

        // Xử lý các yêu cầu action mới
        List<PermissionActionModelRaw> permissionActionModelRawList = new ArrayList<>();
        List<ModelResponse> modelResponseList = processModelRequest(permissionActionModelRawList, permissionId, permissionRequest);

        log.error("modelResponseList:{}", modelResponseList);
        AuthorizeResponse authorizeResponse = createAuthorizeResponse(savedPermission, modelResponseList);

        // Lưu quyền mới
        permissionActionModelRawRepository.saveAll(permissionActionModelRawList);

        return authorizeResponse;
    }


    private Permission createOrUpdatePermission(CreatePermissionRequest permissionRequest, Integer permissionId) {
        Permission existingPermission = permissionRepository.findById(permissionId)
                .orElseGet(() -> {
                    Permission newPermission = new Permission();
                    newPermission.setName(permissionRequest.getName());
                    return permissionRepository.save(newPermission);
                });

        existingPermission.setName(permissionRequest.getName());
        return permissionRepository.save(existingPermission);
    }


    private void clearPermissionActionsForUpdate(Integer permissionId) {
        List<PermissionActionModelRaw> permissionActions = permissionActionModelRawRepository.findByPermissionId(permissionId);
        if (!permissionActions.isEmpty()) {
            deletionUtil.deleteByEntityParentIdOnTable(permissionId, "permission_model_action_raw");
        }
    }


    private List<ModelResponse> processModelRequest(List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId, CreatePermissionRequest permissionRequest) {
        List<ModelResponse> modelResponseList = new ArrayList<>();
        // Duyệt qua danh sách modelRequestList và xử lý từng mẫu
        for (CreateModelRequest modelRequest : permissionRequest.getModelRequestList()) {
            ModelResponse modelResponse = new ModelResponse();
            modelResponse.setModelId(modelRequest.getId());
            List<ActionResponse> actionResponseList = processActionRequests(modelRequest.getActionRequestList(), permissionActionModelRawList, permissionId, modelRequest.getId());
            modelResponse.setActionResponses(actionResponseList);
            modelResponseList.add(modelResponse);
        }
        return modelResponseList;
    }


    private List<ActionResponse> processActionRequests(List<CreateActionRequest> actionRequestList, List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId, Integer modelId) {
        List<ActionResponse> actionResponseList = new ArrayList<>();
        for (CreateActionRequest actionRequest : actionRequestList) {
            ActionResponse actionResponse = new ActionResponse();
            actionResponse.setActionId(actionRequest.getActionId());
            List<RawResponse> rawResponseList = processRawRequests(actionRequest.getRawRequests(), permissionActionModelRawList, permissionId, modelId, actionRequest.getActionId());
            actionResponse.setRawResponse(rawResponseList);
            actionResponseList.add(actionResponse);
        }
        return actionResponseList;
    }



    private List<RawResponse> processRawRequests(List<CreateRawRequest> rawRequestList, List<PermissionActionModelRaw> permissionActionModelRawList, Integer permissionId, Integer modelId, Integer actionId) {
        List<RawResponse> rawResponseList = new ArrayList<>();
        if (rawRequestList == null || rawRequestList.isEmpty()) {
            // Tạo PermissionActionModelRaw với null rawId
            permissionActionModelRawList.add(createPermissionActionModelRaw(permissionId, modelId, actionId, null));
        } else {
            for (CreateRawRequest rawRequest : rawRequestList) {
                RawResponse rawResponse = new RawResponse();
                rawResponse.setRawId(rawRequest.getRawId());
                rawResponseList.add(rawResponse);

                // Tạo PermissionActionModelRaw với rawId thực tế
                permissionActionModelRawList.add(createPermissionActionModelRaw(permissionId, modelId, actionId, rawRequest.getRawId()));
            }
        }
        return rawResponseList;
    }


    private PermissionActionModelRaw createPermissionActionModelRaw(Integer permissionId, Integer modelId, Integer actionId, Integer rawId) {

        log.error("permissionId:{}",permissionId);

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND,"không thấy permission này"));
        Action action = actionsRepository.findById(actionId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        Models models = modelsRepository.findById(modelId).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


        PermissionActionModelRaw permissionActionModelRaw = new PermissionActionModelRaw();
        permissionActionModelRaw.setId(UUID.randomUUID().toString());
        permissionActionModelRaw.setPermissionId(permission.getId());
        permissionActionModelRaw.setActionId(action.getId());
        permissionActionModelRaw.setModelId(models.getId());
        if (rawId != null) {
            permissionActionModelRaw.setRawId(rawId);
        }else {
            permissionActionModelRaw.setRawId(null);
        }

        return permissionActionModelRaw;
    }


    private AuthorizeResponse createAuthorizeResponse(Permission savedPermission, List<ModelResponse> modelResponseList) {
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setId(savedPermission.getId());
        permissionResponse.setName(savedPermission.getName());
        permissionResponse.setModelResponses(modelResponseList);
        authorizeResponse.setPermissonResponse(permissionResponse);

        return authorizeResponse;
    }


}

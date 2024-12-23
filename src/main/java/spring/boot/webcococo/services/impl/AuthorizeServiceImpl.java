package spring.boot.webcococo.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.*;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.mapping.PermissionModelActionRawMapping;
import spring.boot.webcococo.models.pojos.CustomUserDetails;
import spring.boot.webcococo.models.requests.*;
import spring.boot.webcococo.models.response.*;
import spring.boot.webcococo.repositories.*;
import spring.boot.webcococo.services.IAuthorizeService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.*;
import java.util.stream.Collectors;


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
    private final ModelsRepository modelsRepository;
    private final UsersPermissionRepository usersPermissionRepository;

    private final JdbcTemplate jdbcTemplate;


    @Override
    public AuthorizeResponse getAuthor(Integer permissionId) {

        try {
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            String query = """
                       SELECT p.id AS permission_id\s
                       ,p.name AS permission_name
                       ,m.id AS model_id
                       ,m.name AS model_name
                       ,m.description AS model_description
                       ,a.id  AS action_id
                       ,a.name  AS action_name
                       ,a.description  AS action_description
                       ,pamr.raw_id AS raw_id
                       FROM permission_model_action_raw pamr
                       LEFT JOIN models m ON pamr.model_id = m.id
                       LEFT JOIN actions a ON pamr.action_id = a.id
                       JOIN permissions p ON pamr.permission_id  = p.id
                       WHERE p.id = ?;
                    """;
            return jdbcTemplate.query(query, new Object[]{permission.getId()}, rs -> {
                Set<PermissionModelActionRawMapping> permissionModelActionRawMappings = PermissionModelActionRawMapping.toPermissionModelActionMapping(rs);
                log.error("permissionModelActionRawMappings:{}",permissionModelActionRawMappings.size());
                return mapToAuthorizeResponse(permissionModelActionRawMappings.stream().toList());
            });

        } catch (Exception e) {
            throw e;
        }
    }


    private AuthorizeResponse mapToAuthorizeResponse(List<PermissionModelActionRawMapping> permissionModelActionRawMappings) {

        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        PermissionModelActionRawMapping permissionModelActionRawMapping = permissionModelActionRawMappings.iterator().next();
        PermissionResponse permissionResponse = new PermissionResponse(permissionModelActionRawMapping.getPermissionId(), permissionModelActionRawMapping.getPermissionName(),mapToModelResponseList(permissionModelActionRawMappings) );

        authorizeResponse.setPermissonResponse(permissionResponse);
        return authorizeResponse;
    }




    private List<ModelResponse> mapToModelResponseList(List<PermissionModelActionRawMapping> permissionModelActionRawMappings) {
        // Bước 1: Nhóm các đối tượng theo modelId
        Map<Integer, List<PermissionModelActionRawMapping>> groupByModelId = permissionModelActionRawMappings.stream()
                .collect(Collectors.groupingBy(PermissionModelActionRawMapping::getModelId, Collectors.toList()));

        // Bước 2: Ánh xạ các nhóm thành ModelResponse
        return groupByModelId.entrySet().stream()
                .map(entry -> mapToModelResponse(entry.getValue()))  // Ánh xạ các nhóm thành ModelResponse
                .collect(Collectors.toList());  // Thu thập kết quả vào Set<ModelResponse>
    }











    private ModelResponse mapToModelResponse(List<PermissionModelActionRawMapping> modelActionRawMappings) {

        PermissionModelActionRawMapping modelMapping =  modelActionRawMappings.iterator().next();
        ModelResponse modelResponse = new ModelResponse(modelMapping.getModelId(),modelMapping.getModelName(),modelMapping.getModelDescription(), mapToActionResponseList(modelActionRawMappings)) ;

        return modelResponse;
    }





    private List<ActionResponse> mapToActionResponseList(List<PermissionModelActionRawMapping> modelActionRawMappings) {

        Map<Integer, List<PermissionModelActionRawMapping>> groupByActionId = modelActionRawMappings.stream()
                .collect(Collectors.groupingBy(PermissionModelActionRawMapping::getActionId, Collectors.toList()));

        return groupByActionId.entrySet().stream()
                .map(entry -> {
                    PermissionModelActionRawMapping actionEntry = entry.getValue().getFirst(); // Lấy phần tử đầu tiên
                    List<RawResponse> rawResponses = mapToRawResponseList(entry.getValue());

                    // Nếu danh sách RawResponse rỗng, đặt RawIdList là null
                    ActionResponse actionResponse = new ActionResponse(
                            actionEntry.getActionId(),
                            actionEntry.getActionName(),
                            actionEntry.getActionDescription(),
                            rawResponses.isEmpty() ? null : rawResponses // Nếu rỗng, đặt là null
                    );

                    return actionResponse;
                })
                .collect(Collectors.toList());
    }






    private List<RawResponse> mapToRawResponseList(List<PermissionModelActionRawMapping> actionRawMappings) {
        return actionRawMappings.stream()
                .filter(actionRawMapping -> actionRawMapping.getRawId() != null && actionRawMapping.getRawId() != 0) // Loại bỏ null và 0
                .map(actionRawMapping -> new RawResponse(actionRawMapping.getRawId()))
                .collect(Collectors.toList());
    }

















    @Override
    public UserPermissionResponse addAuthor(AddAuthorForUserRequest permissionRequest) {
        String emailUser = permissionRequest.getUserEmail();
        Integer permissionId = permissionRequest.getPermissionId();

        // Tìm người dùng theo email
        Users user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy user"));

        // Kiểm tra quyền đã tồn tại cho người dùng
        if (usersPermissionRepository.findByUserIdAndPermissionId(user.getId(), permissionId).isPresent()) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED, "DA TON TAI QUYEN NAY VOI NGUOI NAY");
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


    private void authenticateAndAuthorize() {
        // Lấy thông tin xác thực từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu không có thông tin xác thực
        if (authentication == null) {
            log.error("Không tìm thấy thông tin xác thực");
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "Người dùng chưa được xác thực");
        }

        // Lấy thông tin principal từ authentication
        Object principal = authentication.getPrincipal();

        // Kiểm tra nếu principal không phải là một instance của CustomUserDetails
        if (!(principal instanceof CustomUserDetails)) {
            log.warn("Principal không phải là instance của CustomUserDetails. Principal: {}", principal);
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED, "Người dùng không hợp lệ");
        }

        // Ép kiểu principal sang CustomUserDetails
//        CustomUserDetails userDetails = (CustomUserDetails) principal;

//        // Kiểm tra xem người dùng có quyền "PERMISSION_ADMIN" hay không
//        boolean isAdmin = userDetails.getAuthorities().stream()
//                .anyMatch(permission -> "PERMISSION_ADMIN".equals(permission.getAuthority()));
//
//        // Nếu không có quyền ADMIN, ném ngoại lệ
//        if (!isAdmin) {
//            throw new AppException(ErrorCodeEnum.UNAUTHORIZED, "USER không có quyền tạo role");
//        }


        // Trả về thông tin người dùng đã xác thực

    }


    @Override
    @Transactional
    public AuthorizeResponse createAuthor(CreatePermissionRequest permissionRequest) {
        try {

            authenticateAndAuthorize();


            // Tạo hoặc cập nhật quyền
            Permission savedPermission = createOrUpdatePermission(permissionRequest);


            // Xử lý các yêu cầu liên quan đến actions
            List<PermissionActionModelRaw> permissionActionModelRawList = new ArrayList<>();
            List<ModelResponse> modelResponseList = processModelRequest(permissionActionModelRawList, savedPermission.getId(), permissionRequest);

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
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public AuthorizeResponse updateAuthor(CreatePermissionRequest permissionRequest, Integer permissionId) {
        authenticateAndAuthorize();

        if (permissionId == null) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND);
        }
        // Tạo hoặc cập nhật quyền
        Permission savedPermission = createOrUpdatePermission(permissionRequest);

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


    @Transactional
    protected Permission createOrUpdatePermission(CreatePermissionRequest permissionRequest) {
        Permission existingPermission = permissionRepository.findById(permissionRequest.getId() == null ? 0 : permissionRequest.getId())
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

        log.error("permissionId:{}", permissionId);
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "không thấy permission này"));
        Action action = actionsRepository.findById(actionId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        Models models = modelsRepository.findById(modelId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


        PermissionActionModelRaw permissionActionModelRaw = new PermissionActionModelRaw();
        permissionActionModelRaw.setId(UUID.randomUUID().toString());
        permissionActionModelRaw.setPermissionId(permission.getId());
        permissionActionModelRaw.setActionId(action.getId());
        permissionActionModelRaw.setModelId(models.getId());
        if (rawId != null) {
            permissionActionModelRaw.setRawId(rawId);
        } else {
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

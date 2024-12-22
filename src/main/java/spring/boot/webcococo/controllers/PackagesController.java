package spring.boot.webcococo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.models.requests.PackagesRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.PackagesResponse;
import spring.boot.webcococo.redis.RedisProducts.impl.PackageRedisImpl;
import spring.boot.webcococo.repositories.ActionsRepository;
import spring.boot.webcococo.services.impl.PackagesServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.sql.SQLException;
import java.util.Set;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/package")
public class PackagesController {

    private final PackagesServiceImpl packagesService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ActionsRepository actionsRepository;
    private final PackageRedisImpl packageRedis;
//
//    @GetMapping(value = "/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ActionResponse getActions(@PathVariable Integer id) {
//        log.error("Đã vào đây actions controller");
//        Action action = actionsService.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
//        return ActionResponse.builder()
//                .name(action.getName()) // Gán các trường từ model
//                .description(action.getDescription()) // Gán các trường khác từ model
//                .build();
//    }
//
//    @GetMapping(value = "")
//    @ResponseStatus(HttpStatus.OK)
//    public List<ActionResponse> getAllActions() {
//        log.error("Đã vào đây actions controller");
//        List<Action> actions = actionsService.findAll();
//        return actions.stream()
//                .map(action -> ActionResponse.builder()
//                        .name(action.getName()) // Gán các trường từ model
//                        .description(action.getDescription()) // Gán các trường khác từ model
//                        .build())
//                .toList();
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse createPackages(@RequestBody @Valid PackagesRequest packagesRequest ) {
        try {

            log.error("đa vao controller");
            PackagesResponse response = packagesService.createPackage(packagesRequest , packagesRequest.getPaymentTermRequest());
            return new ApiResponse(null, "response created successfully", null, response);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updatePackages(@RequestBody @Valid PackagesRequest packagesRequest  ,@PathVariable Integer id) {
        try {
            log.error("đa vao controller");
            PackagesResponse response = packagesService.updatePackage(id,packagesRequest , packagesRequest.getPaymentTermRequest());
            return new ApiResponse(null, "response created successfully", null, response);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deletePackages(@PathVariable Integer id) {

        try {
            packagesService.deletePackage(id);
            return new ApiResponse(null, "delete successfully", null, null);

        }catch (Exception e){
            throw e;
        }
    }



    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPackages(@PathVariable Integer id) {
        try {
            PackagesResponse response =  packagesService.getPackage(id);
            return new ApiResponse(null, "delete successfully", null, response);

        }catch (Exception e){
            throw e;
        }
    }




    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAllPackages(@RequestParam(name = "category_id", defaultValue = "") Integer categorieId) throws SQLException, JsonProcessingException {
        log.error("categorieId: {}", categorieId);
        try {
            long startTime = System.currentTimeMillis();


            Set<PackagesResponse> response = packageRedis.getPackagesFromRedis(null,categorieId);

            if(response == null || response.isEmpty()){
                log.error("khong co du lieu trong cache");
                response = packagesService.getAllPackage(categorieId);
            }

            packageRedis.savePackagesToRedis( response,null , categorieId);


            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.error("Time taken for adding elements to actionSet: " + duration + " milliseconds");



            return new ApiResponse(null, "getAll successfully", null, response);

        } catch (Exception e) {
            throw e;
        }
    }


//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse updateAction(@RequestBody @Valid CreateActionRequest request, @PathVariable Integer id) {
//        Action existingEntity = actionsRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
//        existingEntity.setName(request.getName());
//        existingEntity.setDescription(request.getDescription());
//
//        Action updatedEntity = actionsService.update(existingEntity);
//        return new ApiResponse(null, "Action updated successfully", null, updatedEntity);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse deleteAction(@PathVariable Integer id) {
//        log.error("Đã vào trong delete actions");
//
//        // Truyền enum vào phương thức cascadeDeletion
//
//        actionsService.deleteById(id);
//
//        // Chạy cascade deletion để xóa các bảng liên quan
//        deletionUtil.deleteByParentId(id, "action_id");
//
//        return new ApiResponse(null, "Action deleted successfully", null, null);
//    }
}

package spring.boot.webcococo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.Models;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.CreateModelRequest;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.models.response.ModelResponse;
import spring.boot.webcococo.repositories.ModelsRepository;
import spring.boot.webcococo.services.impl.ModelsServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/models")
public class ModelController {
    private final ModelMapper modelMapper;
    private final ModelsServiceImpl modelService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ModelsRepository modelsRepository;

    @PreAuthorize("@authorUtils.hasAuthor('VIEW','MODEL',null)")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ModelResponse> getModel(@PathVariable Integer id) {
        log.error("Đã vào đây actions controller");
        return modelsRepository.findById(id)
                .map(entity -> ResponseEntity.ok(ModelResponse.builder()
                        .modelName(entity.getName())
                        .modelId(entity.getId())
                        .modelDescription(entity.getDescription())
                        .build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }


    @PreAuthorize("@authorUtils.hasAuthor('VIEW','MODEL',null)")
    @GetMapping(value = "")
    public ResponseEntity<List<ModelResponse>> getAllModels() {
        log.error("Đã vào đây models controller");

        try {
            // Gọi service để lấy danh sách các model
            List<ModelResponse> modelResponses = modelService.findAll()
                    .stream()
                    .map(model -> ModelResponse.builder()
                            .modelId(model.getId())
                            .modelName(model.getName())
                            .build())
                    .toList();

            return ResponseEntity.ok(modelResponses); // Trả về kết quả thành công

        } catch (Exception e) {
            // Log lỗi và trả về phản hồi lỗi
            log.error("Lỗi khi lấy danh sách models: {}", e.getMessage(), e);

            // Trả về mã lỗi và thông báo lỗi cho client
        throw e;
        }
    }




    @PreAuthorize("@authorUtils.hasAuthor('POST','MODEL',null)")
    @PostMapping
    public ResponseEntity<ApiResponse> createModel(@RequestBody CreateModelRequest request) {
        // Thiết lập thông tin đơn hàng
        if (modelMapper.getTypeMap(CreateModelRequest.class, Models.class) == null) {
            modelMapper.typeMap(CreateModelRequest.class, Models.class);
        }

        Models models = new Models();
        modelMapper.map(request, models);

        try {
            Models savedModel = modelService.save(models); // Lưu model vào cơ sở dữ liệu
            if (savedModel != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse(null, "Model created successfully", null, savedModel));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(null, "Error creating model", null, null));
            }
        } catch (Exception e) {
            log.error("Error creating model", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, "Error creating model", null, null));
        }
    }


    @PreAuthorize("@authorUtils.hasAuthor('DELETE','MODEL',null)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteModel(@PathVariable Integer id) {
        log.error("đã vào trong deletemodels");
         try {
             modelService.deleteById(id);
             deletionUtil.deleteByParentId(id, "model_id");
             return ResponseEntity.ok(new ApiResponse(null, "Model deleted successfully", null, null));


         } catch (AppException e) {
             // Nếu có lỗi của ứng dụng, trả về mã lỗi 400 hoặc 500 và thông báo lỗi
             log.error("Error occurred while deleting model", e);
             throw e; // Ném lại ngoại lệ AppException
         } catch (Exception e) {
             // Bắt tất cả các lỗi khác và trả về mã lỗi 500
             log.error("Unexpected error occurred", e);
             throw e; // Ném lại ngoại lệ chung
         }
    }


    @PreAuthorize("@authorUtils.hasAuthor('UPDATE','MODEL',null)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateModel(@RequestBody CreateModelRequest request, @PathVariable Integer id) {
        Optional<Models> existingEntityOptional = modelsRepository.findById(id);

        if (existingEntityOptional.isPresent()) {
            Models existingEntity = existingEntityOptional.get();
            existingEntity.setName(request.getName());
            existingEntity.setDescription(request.getDescription());

            try {
                Models updatedEntity = modelService.update(existingEntity);
                return ResponseEntity.ok(new ApiResponse(null, "Model updated successfully", null, updatedEntity));
            } catch (Exception e) {
                log.error("Error updating model", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(null, "Error updating model", null, null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, "Model not found", null, null));
        }
    }




}
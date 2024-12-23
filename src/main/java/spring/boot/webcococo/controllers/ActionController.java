package spring.boot.webcococo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.boot.webcococo.entities.Action;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.requests.CreateActionRequest;
import spring.boot.webcococo.models.response.ActionResponse;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.repositories.ActionsRepository;
import spring.boot.webcococo.services.impl.ActionsServiceImpl;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/actions")
public class ActionController {

    private final ActionsServiceImpl actionsService;
    private final EntityCascadeDeletionUtil deletionUtil;
    private final ActionsRepository actionsRepository;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActionResponse getActions(@PathVariable Integer id) {

        List<Action> actionList = new ArrayList<>();

        Set<Action> actionSet = new HashSet<>();


        for(int i  = 0 ; i < 100000000;i++){
            Action action = new Action();
            action.setName("Action " );
            action.setDescription("Description for Action " );
            actionList.add(action);
        }


        long startTime = System.currentTimeMillis();



//        for (Action action : actionList) {
//            actionSet.add( action);
//        }


//        actionSet = new HashSet<>(actionList);


        actionSet = actionList.parallelStream()
                .peek(action -> {
                    // In ra tên thread đang thực thi
//                    System.out.println(Thread.currentThread().getName());

                })
                .collect(Collectors.toSet());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
       log.error("Time taken for adding elements to actionSet: " + duration + " milliseconds");





// Stream tuần tự
//        actionSet = actionList.stream()
//                .map(action -> action)
//                .collect(Collectors.toSet());






        log.error("actionList:{}",actionList);
        log.error("Đã vào đây actions controller");
        Action action = actionsService.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
        return ActionResponse.builder()
                .name(action.getName()) // Gán các trường từ model
                .description(action.getDescription()) // Gán các trường khác từ model
                .build();
    }



    @PreAuthorize("@authorUtils.hasAuthor('VIEW','ACTION',null)")

    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public List<ActionResponse> getAllActions() {
        log.error("Đã vào đây actions controller");
        List<Action> actions = actionsService.findAll();
        return actions.stream()
                .map(action -> ActionResponse.builder()
                        .name(action.getName()) // Gán các trường từ model
                        .description(action.getDescription()) // Gán các trường khác từ model
                        .build())
                .toList();
    }


    @PreAuthorize("@authorUtils.hasAuthor('POST','ACTION',null)")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse createAction(@RequestBody @Valid CreateActionRequest request) {
        Action action = new Action();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        Action savedAction = actionsService.save(action);
        return new ApiResponse(null, "Action created successfully", null, savedAction);
    }


    @PreAuthorize("@authorUtils.hasAuthor('UPDATE','ACTION',null)")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updateAction(@RequestBody @Valid CreateActionRequest request, @PathVariable Integer id) {
        Action existingEntity = actionsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Không tìm thấy action với id: " + id));
        existingEntity.setName(request.getName());
        existingEntity.setDescription(request.getDescription());

        Action updatedEntity = actionsService.update(existingEntity);
        return new ApiResponse(null, "Action updated successfully", null, updatedEntity);
    }



    @PreAuthorize("@authorUtils.hasAuthor('DELETE','ACTION',null)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteAction(@PathVariable Integer id) {
        log.error("Đã vào trong delete actions");

        // Truyền enum vào phương thức cascadeDeletion

        actionsService.deleteById(id);

        // Chạy cascade deletion để xóa các bảng liên quan
        deletionUtil.deleteByParentId(id, "action_id");

        return new ApiResponse(null, "Action deleted successfully", null, null);
    }
}

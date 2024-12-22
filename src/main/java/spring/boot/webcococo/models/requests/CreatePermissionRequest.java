package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CreatePermissionRequest {

    @JsonProperty("permission_id")
    private Integer id;

    @JsonProperty("permission_name")
    private String name;


    @NotEmpty(message = "modelIdList cannot be blank")
    @JsonProperty("model_id_list")
    private List<CreateModelRequest> modelRequestList;

}
package spring.boot.webcococo.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateModelRequest {
    @JsonProperty("model_id")
    private Integer Id;

    @NotEmpty(message = "model_name cannot be blank")
    @JsonProperty("model_name")
    private String name;

    @JsonProperty("model_description")
    private String description;

//
//    @NotEmpty(message = "actionRequestList cannot be blank")
    @Valid
    @JsonProperty("action_id_list")
    private List<CreateActionRequest> actionRequestList;



}

package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ModelResponse {
    @JsonProperty("id")
    private Integer modelId;

    @JsonProperty("name")
    private String modelName;

    @JsonProperty("description")
    private String modelDescription;

    @JsonProperty("actions")
    private List<ActionResponse> actionResponses;


}

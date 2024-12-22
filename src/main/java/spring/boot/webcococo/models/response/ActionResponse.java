package spring.boot.webcococo.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResponse {

    @JsonProperty("action_id")
    private Integer actionId;

    @JsonProperty("action_name")
    private String name;

    @JsonProperty("action_description")
    private String description;

    @JsonProperty("RawIdList")
    private List<RawResponse>  rawResponse ;
}

package spring.boot.webcococo.models.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


//@JsonInclude(JsonInclude.Include.NON_NULL)// fiel naò null thì không trả về
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T>{
    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("detail_message")
    private String detailMess;

    @JsonProperty("result")
    private T result;
}

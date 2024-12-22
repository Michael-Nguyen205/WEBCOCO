package spring.boot.webcococo.models.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateRawRequest {

    @JsonProperty("raw_id")
    private Integer rawId;
}

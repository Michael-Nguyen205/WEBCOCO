package spring.boot.webcococo.dtos;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GreetingMessageDTO {
    private String content;

}

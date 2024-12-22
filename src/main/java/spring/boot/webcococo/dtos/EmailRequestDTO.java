package spring.boot.webcococo.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequestDTO {
    SenderDTO sender;
    List<RecipientDTO> to;
    String subject;
    String htmlContent;

    // DTO cho Recipient
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RecipientDTO {
        String name;
        String email;
    }

    // DTO cho Sender
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SenderDTO {
        String name;
        String email;
    }

    // DTO cho SendEmailRequest



}

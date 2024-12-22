package spring.boot.webcococo.models.requests;//package spring.boot.webcococo.models.requests;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.bouncycastle.cms.Recipient;
//
//import java.util.List;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class EmailRequest {
//    Sender sender;
//    List<Recipient> to;
//    String subject;
//    String htmlContent;
//
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public class Recipient {
//        String name;
//        String email;
//    }
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public class Sender {
//        String name;
//        String email;
//    }
//
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public class SendEmailRequest {
//        Recipient to;
//        String subject;
//        String htmlContent;
//    }
//}
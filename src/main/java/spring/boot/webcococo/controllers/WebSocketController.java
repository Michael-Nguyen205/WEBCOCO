package spring.boot.webcococo.controllers;//package spring.boot.authenauthor.controllers;
//
////import com.example.pdfconverter.dtos.request.GreetingMessage;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import spring.boot.authenauthor.dtos.GreetingMessageDTO;
//
//
//@Log4j2
//@Controller
//public class WebSocketController {
//
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings") // Tin nhắn sẽ được gửi tới tất cả các client đang subscribe vào "/topic/greetings".
//    public GreetingMessageDTO greeting(String message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        // Lấy thông tin từ header (ví dụ: userId).
//        String userId = headerAccessor.getFirstNativeHeader("userId");
//        log.error("vaooooooooooooo WebSocket");
//
//        // Tạo và trả về đối tượng GreetingMessage với thông điệp phản hồi.
//        return new GreetingMessageDTO("Hello, " + message + "! (UserID: " + userId + ")");
//    }
//}

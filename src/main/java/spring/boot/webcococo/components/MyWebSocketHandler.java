//package spring.boot.webcococo.components;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    // Xử lý sự kiện kết nối thành công
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("Kết nối WebSocket đã được thiết lập: " + session.getId());
//    }
//
//    // Xử lý tin nhắn từ client
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        System.out.println("Nhận tin nhắn từ client: " + message.getPayload());
//        session.sendMessage(new TextMessage("Phản hồi từ server"));
//    }
//
//    // Xử lý lỗi
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        System.out.println("Lỗi xảy ra trong kết nối WebSocket: " + exception.getMessage());
//    }
//
//    // Xử lý khi kết nối bị đóng
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        System.out.println("Kết nối WebSocket đã bị đóng: " + session.getId() + " với lý do: " + status);
//    }
//}
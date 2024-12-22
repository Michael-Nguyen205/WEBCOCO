package spring.boot.webcococo.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Tạo logger để ghi log
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        try {
            // Định nghĩa message broker để publish messages tới các client
            config.enableSimpleBroker("/topic");
            // Các tin nhắn từ client sẽ có prefix "/app"
            config.setApplicationDestinationPrefixes("/app");
        } catch (Exception e) {
            logger.error("Error configuring message broker: {}", e.getMessage(), e);
            throw e;  // Ném lại ngoại lệ nếu cần
        }
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        try {
            // Định nghĩa WebSocket endpoint
            registry.addEndpoint("/ws")
                    .setAllowedOrigins("http://localhost:3006")
             .withSockJS(); // Kích hoạt SockJS fallback nếu cần

            logger.info("WebSocket endpoint '/ws' registered successfully");
        } catch (RuntimeException e) {
            logger.error("Error registering STOMP endpoint: {}", e.getMessage(), e);
            throw e;  // Ném lại ngoại lệ nếu cần xử lý ở nơi khác
        }
    }
}

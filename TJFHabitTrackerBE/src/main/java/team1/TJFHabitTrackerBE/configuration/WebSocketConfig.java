package team1.TJFHabitTrackerBE.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configura il broker di messaggi (es. abbonamenti ai topic)
        config.enableSimpleBroker("/topic");
        // Prefix per i messaggi inviati dal client
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint per stabilire la connessione WebSocket
        registry.addEndpoint("/ws-notifications")
                .setAllowedOrigins("*") // Specifica gli origin se necessario per la sicurezza
                .withSockJS(); // Fallback per browser che non supportano WebSocket
    }
}

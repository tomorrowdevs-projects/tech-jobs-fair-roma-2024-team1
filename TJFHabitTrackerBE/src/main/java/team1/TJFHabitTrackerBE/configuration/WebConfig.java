package team1.TJFHabitTrackerBE.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "https://ritmogiornaliero.netlify.app") // Origini consentite senza barra finale
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Metodi consentiti
                .allowedHeaders("Authorization", "Content-Type") // Header consentiti
                .allowCredentials(true); // Consenti l'invio di credenziali (cookie)
    }
}
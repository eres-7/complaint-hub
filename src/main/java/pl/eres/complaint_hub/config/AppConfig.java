package pl.eres.complaint_hub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI customSwaggerBaseUrl(@Value("${springdoc.server.base-url:http://localhost:8080}") String springdocServerBaseUrl) {
        return new OpenAPI().addServersItem(new Server().url(springdocServerBaseUrl));
    }
}

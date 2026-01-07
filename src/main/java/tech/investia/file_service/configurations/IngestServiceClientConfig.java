package tech.investia.file_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IngestServiceClientConfig {
    @Bean
    WebClient ingestServiceWebClient(@Value("${ingestservice.base-url}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }
}
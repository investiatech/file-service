package tech.investia.file_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class N8nCrawlerClientConfig {
    @Bean
    public WebClient n8nCrawlerWebClient(
            @Value("${n8n.baseUrl}") String baseUrl,
            @Value("${n8n.username}") String username,
            @Value("${n8n.password}") String password
    ) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(20 * 1024 * 1024)) // 20 MB
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .build();
    }
}

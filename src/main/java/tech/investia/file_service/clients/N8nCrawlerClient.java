//package tech.investia.file_service.clients;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Component
//public class N8nCrawlerClient {
//    private final WebClient n8nCrawlerWebClient;
//
//    public N8nCrawlerClient(WebClient n8nCrawlerWebClient, @Value("${n8n.baseUrl}") String url) {
//        this.n8nCrawlerWebClient = n8nCrawlerWebClient;
//    }
//
//    public String crawlHtml(String link) {
//        return n8nCrawlerWebClient.post()
//                .bodyValue(Map.of("link", link))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
//
//}

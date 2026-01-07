//TODO
//package tech.investia.file_service.clients;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Component
//public class IngestServiceClient {
//
//    private final WebClient ingestServiceWebClient;
//
//    public IngestServiceClient(WebClient ingestServiceWebClient) {
//        this.ingestServiceWebClient = ingestServiceWebClient;
//    }
//
//    public void triggerIngestFromFile(String fileId, String authorization) {
//
//        WebClient.RequestBodySpec request = ingestServiceWebClient.post()
//                .uri("/api/ingest/fromFile")
//                .bodyValue(Map.of("fileId", fileId));
//
//        // JWT passthrough (opcjonalny)
//        if (authorization != null && !authorization.isBlank()) {
//            request.header(HttpHeaders.AUTHORIZATION, authorization);
//        }
//
//        request.retrieve()
//                .toBodilessEntity()
//                .block();
//    }
//}

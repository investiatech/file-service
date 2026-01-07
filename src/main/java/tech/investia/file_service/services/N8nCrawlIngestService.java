package tech.investia.file_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tech.investia.file_service.dto.N8nCrawlResponse;
import tech.investia.file_service.dto.ResourceResponse;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Service
public class N8nCrawlIngestService {

    private final WebClient n8nCrawlerWebClient;
    private final FileIngestService fileIngestService;
    private static final Logger log = LoggerFactory.getLogger(N8nCrawlIngestService.class);

    @Value("${n8n.baseUrl}")
    private String webhookPath;

    @Value("${n8n.username}")
    private String username;

    @Value("${n8n.password}")
    private String password;

    public N8nCrawlIngestService(WebClient n8nCrawlerWebClient, FileIngestService fileIngestService) {
        this.n8nCrawlerWebClient = n8nCrawlerWebClient;
        this.fileIngestService = fileIngestService;
    }

    public ResourceResponse crawlAndIngest(String link) throws Exception {

        // Crawl n8n (form-data link)
        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("link", link);
        log.info("link: {}", link);
// To działa
//        String result1 = n8nCrawlerWebClient.post()
//                .uri("https://n8n.easy-ai.it/webhook/e9e561c2-cbce-42b4-9270-c4e3c0cbec17")
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .bodyValue(form)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        System.out.println(result1);

//        MultipartBodyBuilder mb = new MultipartBodyBuilder();
//        mb.part("link", "https://redaktor-online.pl/");

//        MultipartBodyBuilder mb = new MultipartBodyBuilder();
//        mb.part("link", "https://redaktor-online.pl/"); // nazwa pola MUSI być "link"
//
//        MultiValueMap<String, HttpEntity<?>> multipartBody = mb.build();

        List<N8nCrawlResponse> result = n8nCrawlerWebClient.post()
                .uri("https://n8n.easy-ai.it/webhook/e9e561c2-cbce-42b4-9270-c4e3c0cbec17")
                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(form)
                .retrieve()
                .bodyToFlux(N8nCrawlResponse.class)
                .collectList()
                .timeout(Duration.ofMinutes(3))
                .block();

//        List<N8nCrawlResponse> result = n8nCrawlerWebClient.post()
//                .uri("https://n8n.easy-ai.it/webhook/e9e561c2-cbce-42b4-9270-c4e3c0cbec17")
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .accept(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromMultipartData(multipartBody))
//                .retrieve()
//                .bodyToFlux(N8nCrawlResponse.class)
//                .collectList()
//                .block();

//        List<N8nCrawlResponse> result = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(mb.build()))
//                .retrieve()
//                .bodyToFlux(N8nCrawlResponse.class)
//                .collectList()
//                .timeout(Duration.ofMinutes(3))
//                .block();

//        String body = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(mb.build()))
//                .retrieve()
//                .bodyToMono(String.class)
//                .defaultIfEmpty("")
//                .block();

//        log.info("n8n raw body: {}", body);
//
//        String raw = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .accept(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromMultipartData(mb.build()))
//                .retrieve()
//                .bodyToMono(String.class)
//                .defaultIfEmpty("")
//                .block();
//
//        log.info("n8n raw body: {}", raw);


//        List<N8nCrawlResponse> result = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(form))
//                .retrieve()
//                .bodyToFlux(N8nCrawlResponse.class)
//                .collectList()
//                .timeout(Duration.ofMinutes(3))   // dostosuj
//                .block();

//        ResponseEntity<String> resp = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(form))
//                .retrieve()
//                .toEntity(String.class)
//                .block();
//
//        log.info("n8n status={} headers={} body={}",
//                resp != null ? resp.getStatusCode() : null,
//                resp != null ? resp.getHeaders() : null,
//                resp != null ? resp.getBody() : null);

//        List<N8nCrawlResponse> result = n8nCrawlerWebClient.post()
//                .uri(webhookPath)
//                .headers(h -> h.setBasicAuth("admin", "quickSteps89"))
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData(form))
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, respo ->
//                        respo.bodyToMono(String.class)
//                                .defaultIfEmpty("")
//                                .flatMap(body -> Mono.error(
//                                        new RuntimeException("n8n error " + respo.statusCode() + " body=" + body)
//                                ))
//                )
//                .bodyToFlux(N8nCrawlResponse.class)
//                .collectList()
//                .block();

        if (result == null || result.isEmpty() || result.getFirst().body() == null || result.getFirst().body().isBlank()) {
            throw new IllegalStateException("n8n returned empty HTML for link=" + link);
        }

        String html = result.getFirst().body();

        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        String objectName = "crawl-" + System.currentTimeMillis() + ".html";

        return fileIngestService.uploadAndRecord(
                bytes,
                objectName,
                true
//                authorization
        );

//        // mongo write metadata
//        FileResource fileResource = new FileResource();
//        fileResource.setName(objectName);
//        fileResource.setResourceType(WEBSITE);
//        fileResource.setLanguage(PL);
//        fileResource.setResourceStatus(PENDING);
//        fileResource.setBucket(minioStorageService.bucket());
//        fileResource.setObjectKey(uploadResult.objectKey());
//        fileResource.setEtag(uploadResult.etag());
//        fileResource.setSize(uploadResult.size());
//        fileResource.setContentType(uploadResult.contentType());
//        fileResource.setDetails(Map.of("sourceUrl", link));
//
//        FileResource saved = fileResourceRepository.save(fileResource);
//        String fileId = saved.getId();
//
//
//        // ingest-service
//        ingestServiceWebClient.post()
//                .uri("/api/ingest/fromFile")
//                .header(HttpHeaders.AUTHORIZATION, authorization)
//                .bodyValue(new IngestFromFileRequest(saved.getId()))
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//
//        return new ResourceResponse(
//                saved.getId(),
//                saved.getName(),
//                saved.getResourceType(),
//                saved.getLanguage(),
//                Map.of(
//                        "bucket", saved.getBucket(),
//                        "objectKey", saved.getObjectKey(),
//                        "size", saved.getSize(),
//                        "contentType", saved.getContentType(),
//                        "etag", saved.getEtag()
//                ),
//                saved.getResourceStatus()
//        );


    }
}

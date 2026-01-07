package tech.investia.file_service.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.investia.file_service.dto.ResourceResponse;
import tech.investia.file_service.dto.WebsiteCrawlRequest;
import tech.investia.file_service.services.N8nCrawlIngestService;
import tech.investia.file_service.services.WebsiteCrawlService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/resource/website")
public class WebsiteCrawlController {

    private final WebsiteCrawlService websiteCrawlService;
    private final N8nCrawlIngestService n8nCrawlIngestService;

    public WebsiteCrawlController(WebsiteCrawlService websiteCrawlService, N8nCrawlIngestService n8nCrawlIngestService) {
        this.websiteCrawlService = websiteCrawlService;
        this.n8nCrawlIngestService = n8nCrawlIngestService;
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody WebsiteCrawlRequest request) {
        ResourceResponse response = websiteCrawlService.createWebsiteCrawl(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResourceResponse>> listAll() {

        return ResponseEntity.ok(websiteCrawlService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getOneById(@PathVariable String id) {

        return websiteCrawlService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOneById(@PathVariable String id) {

        return ResponseEntity.ok(websiteCrawlService.deleteById(id));
    }

    @PostMapping("/n8n")
    public ResponseEntity<List<ResourceResponse>> crawl(@RequestBody WebsiteCrawlRequest websiteCrawlRequest) throws Exception {

        List<ResourceResponse> responseList = new ArrayList<>();
        String domain = normalizeDomain(websiteCrawlRequest.domain());
        for (String path : websiteCrawlRequest.urls()) {
            String finalLink = domain + normalizePath(path);
            responseList.add(
                    n8nCrawlIngestService.crawlAndIngest(finalLink)
            );
        }
        return ResponseEntity.ok(responseList);
//        return ResponseEntity.ok(n8nCrawlIngestService.crawlAndIngest(finalLink, authorization));
    }

    private String normalizeDomain(String domain) {
        if (domain.endsWith("/")) {
            return domain.substring(0, domain.length() - 1);
        }
        return domain;
    }

    private String normalizePath(String path) {
        if (!path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

}

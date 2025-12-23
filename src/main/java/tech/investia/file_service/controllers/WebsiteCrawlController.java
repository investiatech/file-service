package tech.investia.file_service.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.investia.file_service.dto.ResourceResponse;
import tech.investia.file_service.dto.WebsiteCrawlRequest;
import tech.investia.file_service.services.WebsiteCrawlService;

import java.util.List;

@RestController
@RequestMapping("/api/resource/website")
public class WebsiteCrawlController {

    private final WebsiteCrawlService websiteCrawlService;

    public WebsiteCrawlController(WebsiteCrawlService websiteCrawlService) {
        this.websiteCrawlService = websiteCrawlService;
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

}

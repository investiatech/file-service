package tech.investia.file_service.services;

import org.springframework.stereotype.Service;
import tech.investia.file_service.configurations.N8nCrawlerClientConfig;

@Service
public class CrawlIngestService {

    private final N8nCrawlerClientConfig n8nCrawlerClientConfig;
    private final FileIngestService fileIngestService;

    public CrawlIngestService(N8nCrawlerClientConfig n8nCrawlerClientConfig, FileIngestService fileIngestService) {
        this.n8nCrawlerClientConfig = n8nCrawlerClientConfig;
        this.fileIngestService = fileIngestService;
    }

}

package tech.investia.file_service.services;

import org.springframework.stereotype.Service;
import tech.investia.file_service.dto.ResourceResponse;
import tech.investia.file_service.dto.WebsiteCrawlRequest;
import tech.investia.file_service.mappers.WebsiteCrawlMapper;
import tech.investia.file_service.models.WebsiteCrawl;
import tech.investia.file_service.repositories.WebsiteCrawlRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteCrawlService {
    private final WebsiteCrawlRepository websiteCrawlRepository;

    public WebsiteCrawlService(WebsiteCrawlRepository websiteCrawlRepository) {
        this.websiteCrawlRepository = websiteCrawlRepository;
    }

    public ResourceResponse createWebsiteCrawl(WebsiteCrawlRequest websiteCrawlRequest) {
        WebsiteCrawl websiteCrawl = WebsiteCrawlMapper.mapToWebsiteCrawl(websiteCrawlRequest);
        WebsiteCrawl saved = websiteCrawlRepository.save(websiteCrawl);

        return WebsiteCrawlMapper.mapToResourceResponse(saved);
    }

    public List<ResourceResponse> findAll() {
        return websiteCrawlRepository.findAll()
                .stream()
                .map(WebsiteCrawlMapper::mapToResourceResponse)
                .toList();
    }

    public Optional<ResourceResponse> findById(String id) {

        return websiteCrawlRepository.findById(id)
                .map(WebsiteCrawlMapper::mapToResourceResponse);
    }

    public String deleteById(String id) {
        if (!websiteCrawlRepository.existsById(id)) return "not found";
        websiteCrawlRepository.deleteById(id);

        return id;
    }

}

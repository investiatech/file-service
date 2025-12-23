package tech.investia.file_service.mappers;

import org.springframework.stereotype.Component;
import tech.investia.file_service.dto.ResourceResponse;
import tech.investia.file_service.dto.ResourceType;
import tech.investia.file_service.models.FileResource;
import tech.investia.file_service.models.WebsiteCrawl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ResourceMapper {

    public ResourceResponse toResourceResponse(FileResource fileResource) {
        Map<String, Object> details = Optional.ofNullable(fileResource.getDetails())
                .orElse(new HashMap<String, Object>());

        details.putIfAbsent("bucket", fileResource.getBucket());
        details.putIfAbsent("objectKey", fileResource.getObjectKey());
        details.putIfAbsent("size", fileResource.getSize());
        details.putIfAbsent("contentType", fileResource.getContentType());

        return new ResourceResponse(
                fileResource.getId(),
                fileResource.getName(),
                fileResource.getResourceType(),
                fileResource.getLanguage(),
                details,
                fileResource.getResourceStatus()
        );
    }

    public ResourceResponse toResourceResponse(WebsiteCrawl websiteCrawl) {
        Map<String, Object> details = Optional.ofNullable(websiteCrawl.getDetails())
                .orElse(new HashMap<String, Object>());

        details.putIfAbsent("domain", websiteCrawl.getDetails());
        details.putIfAbsent("urls", websiteCrawl.getUrls());
        details.putIfAbsent("useSitemap", websiteCrawl.isUseSitemap());
        details.putIfAbsent("sitemapUrl", websiteCrawl.getSitemapUrl());

        return new ResourceResponse(
                websiteCrawl.getId(),
                websiteCrawl.getDomain(),
                websiteCrawl.getResourceType(),
                websiteCrawl.getLanguage(),
                details,
                websiteCrawl.getResourceStatus()
        );
    }

}

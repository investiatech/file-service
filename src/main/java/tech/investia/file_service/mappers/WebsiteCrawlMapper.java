package tech.investia.file_service.mappers;

import tech.investia.file_service.dto.*;
import tech.investia.file_service.models.WebsiteCrawl;

import java.util.Map;
import java.util.UUID;

public class WebsiteCrawlMapper {

    public static WebsiteCrawl mapToWebsiteCrawl(WebsiteCrawlRequest websiteCrawlRequest) {
        WebsiteCrawl websiteCrawl = new WebsiteCrawl();

        websiteCrawl.setId(UUID.randomUUID().toString());
        websiteCrawl.setDomain(websiteCrawlRequest.domain());
        websiteCrawl.setUrls(websiteCrawlRequest.urls());
        websiteCrawl.setCrawlDepth(websiteCrawlRequest.crawlDepth());
        websiteCrawl.setUseSitemap(websiteCrawlRequest.useSitemap());
        websiteCrawl.setSitemapUrl(websiteCrawlRequest.sitemapUrl());

        if (websiteCrawlRequest.exclusions() != null) {
            WebsiteCrawl.Exclusions exclusions = new WebsiteCrawl.Exclusions();
            exclusions.setPaths(websiteCrawlRequest.exclusions().paths());
            websiteCrawl.setExclusions(exclusions);
        }

        websiteCrawl.setResourceStatus(ResourceStatus.PENDING);
        websiteCrawl.setLanguage(Language.PL);

        return websiteCrawl;
    }

//    public record ResourceResponse(
//            String id,
//            String name,
//            ResourceType type,
//            tech.investia.file_service.dto.ResourceResponse.Language language,
//            Map<String, Object> details,
//            ResourceStatus status
//    ) {
//        public enum Type {FILE, WEBSITE, SOCIAL, TEXT}
//
//        public enum Language {PL, EN}
//
//        public enum Status {PENDING, PROCESSING, COMPLETED, ERROR}
//    }
//

    public static ResourceResponse mapToResourceResponse(WebsiteCrawl websiteCrawl) {

        Map<String, Object> details = Map.of(
                "crawlDepth", websiteCrawl.getCrawlDepth(),
                "useSitemap", websiteCrawl.isUseSitemap(),
                "sitemapUrl", websiteCrawl.getSitemapUrl(),
                "urlsCount", websiteCrawl.getUrls() != null ? websiteCrawl.getUrls().size() : 0
        );

//        Language language = websiteCrawl.getLanguage() != null
//                ? safeEnum(Language.class, websiteCrawl.getLanguage(), Language.PL)
//                : Language.PL;
//
//        ResourceStatus status = websiteCrawl.getResourceStatus() != null
//                ? safeEnum(ResourceStatus.class, websiteCrawl.getResourceStatus(), ResourceStatus.PENDING)
//                : ResourceStatus.PENDING;

        return new ResourceResponse(
                websiteCrawl.getId(),
                websiteCrawl.getDomain(),
                ResourceType.WEBSITE,
                websiteCrawl.getLanguage(),
                details,
                websiteCrawl.getResourceStatus()
        );
    }

    private static <E extends Enum<E>> E safeEnum(Class<E> type, String value, E fallback) {
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return fallback;
        }
    }

}

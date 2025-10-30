package tech.investia.file_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.investia.file_service.dto.Language;
import tech.investia.file_service.dto.ResourceStatus;
import tech.investia.file_service.dto.ResourceType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Example:
 * {
 * "domain": "onet.pl",
 * "urls": ["/test"],
 * "crawl_depth": 2,
 * "use_sitemap": false,
 * "sitemap_url": "",
 * "exclusions": { "paths": ["/api"] }
 * }
 * Example:
 * {
 * "domain": "https://example.com",
 * "urls": [ // przyjmuje tylko link zgodne zadeklarowaną domeną lub relatywne (w innym wypadku błąd)
 * "https://example.com/about",
 * "/blog"
 * ],
 * "crawl_depth": 2,
 * "use_sitemap": true,
 * "sitemap_url": "https://example.com/sitemap.xml",
 * "exclusions": {
 * "paths": ["/admin", "/private"],
 * },
 * }
 * Response: public record ResourceResponse(
 * String id,
 * String name,
 * ResourceType type,
 * Language language,
 * Map<String, Object> details,
 * ResourceStatus status
 * ) {
 * public enum Type {FILE, WEBSITE, SOCIAL, TEXT}
 * <p>
 * public enum Language {PL, EN}
 * <p>
 * public enum Status {PENDING, PROCESSING, COMPLETED, ERROR}
 * }
 */


//TODO: Create class super Resource -> WebsiteCrawl
@Document(collection = "website_crawls")
public class WebsiteCrawl {
    @Id
    private String id;
    private String domain;
    private List<String> urls;
    private int crawlDepth;
    private boolean useSitemap;
    private String sitemapUrl;
    private Exclusions exclusions;
    private ResourceType resourceType;
    private Language language;
    private Map<String, Object> details;
    private ResourceStatus resourceStatus;

    public static class Exclusions {
        private List<String> paths;

        public List<String> getPaths() {
            return paths;
        }

        public void setPaths(List<String> paths) {
            this.paths = paths;
        }
    }

    public WebsiteCrawl() {
    }

    public WebsiteCrawl(String id,
                        String domain,
                        List<String> urls,
                        int crawlDepth,
                        boolean useSitemap,
                        String sitemapUrl,
                        Exclusions exclusions,
                        ResourceType resourceType,
                        Language language,
                        Map<String, Object> details,
                        ResourceStatus resourceStatus) {
        this.id = id;
        this.domain = domain;
        this.urls = urls;
        this.crawlDepth = crawlDepth;
        this.useSitemap = useSitemap;
        this.sitemapUrl = sitemapUrl;
        this.exclusions = exclusions;
        this.resourceType = resourceType;
        this.language = language;
        this.details = details;
        this.resourceStatus = resourceStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(int crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    public boolean isUseSitemap() {
        return useSitemap;
    }

    public void setUseSitemap(boolean useSitemap) {
        this.useSitemap = useSitemap;
    }

    public String getSitemapUrl() {
        return sitemapUrl;
    }

    public void setSitemapUrl(String sitemapUrl) {
        this.sitemapUrl = sitemapUrl;
    }

    public Exclusions getExclusions() {
        return exclusions;
    }

    public void setExclusions(Exclusions exclusions) {
        this.exclusions = exclusions;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public ResourceStatus getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(ResourceStatus resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

}

package tech.investia.file_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Example:
 * DTO for incoming website crawling configuration.
 * Matches JSON like:
 * {
 * "domain": "onet.pl",
 * "urls": ["/test"],
 * "crawl_depth": 2,
 * "use_sitemap": false,
 * "sitemap_url": "",
 * "exclusions": { "paths": ["/api"] }
 * }
 */

public record WebsiteCrawlRequest(
        @NotBlank String domain,

        @NotNull List<String> urls,

        @JsonProperty("crawl_depth")
        int crawlDepth,

        @JsonProperty("use_sitemap")
        boolean useSitemap,

        @JsonProperty("sitemap_url")
        String sitemapUrl,

        Exclusions exclusions
) {
    public record Exclusions(
            List<String> paths
    ) {
    }
}





package tech.investia.file_service.dto;

import java.util.Map;

public record N8nCrawlResponse(
        String body,
        Map<String, String> headers,
        Integer statusCode,
        String url
) {
    public boolean isSuccess() {
        return statusCode != null && statusCode >= 200 && statusCode < 300;
    }

}

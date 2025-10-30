package tech.investia.file_service.dto;

import java.util.Map;

public record ResourceResponse(
        String id,
        String name,
        ResourceType type,
        Language language,
        Map<String, Object> details,
        ResourceStatus status
) {
}
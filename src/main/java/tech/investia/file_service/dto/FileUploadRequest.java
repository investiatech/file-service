package tech.investia.file_service.dto;

public record FileUploadRequest(
        String name,
        boolean overwrite
) {
}

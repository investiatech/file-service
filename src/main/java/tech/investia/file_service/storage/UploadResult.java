package tech.investia.file_service.storage;

public record UploadResult(
        String objectKey,
        String etag,
        long size,
        String contentType
) {
}

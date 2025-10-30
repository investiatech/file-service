package tech.investia.file_service.dto;

public enum ResourceStatus {
    PENDING,      // waiting to be processed
    PROCESSING,   // currently being processed
    COMPLETED,    // successfully indexed
    ERROR         // failed
}
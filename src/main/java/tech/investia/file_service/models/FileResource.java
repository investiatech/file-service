package tech.investia.file_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.investia.file_service.dto.Language;
import tech.investia.file_service.dto.ResourceStatus;
import tech.investia.file_service.dto.ResourceType;

import java.util.Map;

@Document(collection = "file_resource")
public class FileResource {

    @Id
    private String id;
    private String name;
    private ResourceType resourceType;
    private Language language;
    private ResourceStatus resourceStatus;
    private Map<String, Object> details;

    private String bucket;
    private String objectKey;
    private String etag;
    private long size;
    private String contentType;
    private String checksumSha256;

    public FileResource() {
    }

    public FileResource(String id, String name, ResourceType resourceType, Language language, ResourceStatus resourceStatus, Map<String, Object> details, String bucket, String objectKey, String etag, long size, String contentType, String checksumSha256) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.language = language;
        this.resourceStatus = resourceStatus;
        this.details = details;
        this.bucket = bucket;
        this.objectKey = objectKey;
        this.etag = etag;
        this.size = size;
        this.contentType = contentType;
        this.checksumSha256 = checksumSha256;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ResourceStatus getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(ResourceStatus resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getChecksumSha256() {
        return checksumSha256;
    }

    public void setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
    }
}

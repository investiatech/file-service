package tech.investia.file_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import tech.investia.file_service.dto.*;
import tech.investia.file_service.models.FileResource;
import tech.investia.file_service.repositories.FileResourceRepository;
import tech.investia.file_service.storage.UploadResult;

import java.util.Map;
import java.util.Objects;

@Service
public class FileIngestService {
    private static final Logger log = LoggerFactory.getLogger(FileIngestService.class);
    private final MinioStorageService minioStorageService;
    private final FileResourceRepository fileResourceRepository;
    private final WebClient ingestServiceWebClient;

    public FileIngestService(MinioStorageService minioStorageService, FileResourceRepository fileResourceRepository, WebClient ingestServiceWebClient) {
        this.minioStorageService = minioStorageService;
        this.fileResourceRepository = fileResourceRepository;
        this.ingestServiceWebClient = ingestServiceWebClient;
    }

    public ResourceResponse uploadAndRecord(MultipartFile file, String objectName, boolean overwrite, String authorization) throws Exception {
        UploadResult uploadResult = minioStorageService.upload(file, objectName, overwrite);

        FileResource fileResource = new FileResource();
        fileResource.setName(file.getOriginalFilename());
        fileResource.setResourceType(ResourceType.FILE);
        fileResource.setLanguage(Language.PL);
        fileResource.setResourceStatus(ResourceStatus.PENDING);
        fileResource.setBucket(minioStorageService.bucket());
        fileResource.setObjectKey(uploadResult.objectKey());
        fileResource.setEtag(uploadResult.etag());
        fileResource.setSize(uploadResult.size());
        fileResource.setContentType(uploadResult.contentType());
        fileResource.setDetails(Map.of("OriginalFilename", Objects.requireNonNull(file.getOriginalFilename())));

        FileResource saved = fileResourceRepository.save(fileResource);

        //call ingest-service
        ingestServiceWebClient.post()
                .uri("/api/ingest/fromFile")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .bodyValue(new IngestFromFileRequest(saved.getId()))
                .retrieve()
                .toBodilessEntity()
                .block();

        return new ResourceResponse(
                saved.getId(),
                saved.getName(),
                saved.getResourceType(),
                saved.getLanguage(),
                Map.of("bucket", saved.getBucket(),
                        "objectKey", saved.getObjectKey(),
                        "size", saved.getSize(),
                        "contentType", saved.getContentType(),
                        "etag", saved.getEtag()
                ),
                saved.getResourceStatus()
        );
    }

    public void triggerIngest(String fileId, String authorization) {
        System.out.println(authorization);
        log.info("AUTH={}", authorization);

//        ingestServiceWebClient.post()
//                .uri("/api/ingest/fromFile")
//                .header(HttpHeaders.AUTHORIZATION, authorization)
//                .bodyValue(new IngestFromFileRequest(fileId))
//                .retrieve()
//                .toBodilessEntity()
//                .block();

        ingestServiceWebClient.post()
                .uri("/api/ingest/fromFile")
                .header(HttpHeaders.AUTHORIZATION, authorization)   // <- MUSI BYĆ
                .bodyValue(Map.of("fileId", fileId))                // albo IngestFromFileRequest
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}

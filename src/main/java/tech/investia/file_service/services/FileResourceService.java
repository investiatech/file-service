package tech.investia.file_service.services;

import com.sun.nio.sctp.IllegalReceiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.investia.file_service.models.FileResource;
import tech.investia.file_service.repositories.FileResourceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FileResourceService {
    private static final Logger log = LoggerFactory.getLogger(FileResourceService.class);
    private final FileResourceRepository fileResourceRepository;
    private final MinioStorageService minioStorageService;
    private final WebClient ingestServiceWebClient;

    public FileResourceService(FileResourceRepository fileResourceRepository, MinioStorageService minioStorageService, WebClient ingestServiceWebClient) {
        this.fileResourceRepository = fileResourceRepository;
        this.minioStorageService = minioStorageService;
        this.ingestServiceWebClient = ingestServiceWebClient;
    }

    //TODO mapper FileResource -> ResourceResponse
    public List<FileResource> findAll() {

        return fileResourceRepository.findAll();
    }

    public Optional<FileResource> findById(String id) {

        return fileResourceRepository.findById(id);
    }

    public boolean deleteById(String id) {
        FileResource fileResource = fileResourceRepository.findById(id)
                .orElseThrow(() -> new IllegalReceiveException("File id not found" + id));

        // remove file from minio
        try {
            minioStorageService.deleteObject(fileResource.getBucket(), fileResource.getObjectKey());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete object from minio: " + fileResource.getObjectKey() + e);
        }

        //remove from mongo
        fileResourceRepository.deleteById(id);

        //remove from qdrant by ingest-service
        try {
            ingestServiceWebClient.delete()
                    .uri("/api/ingest/deleteByFileId/{fileId}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            log.error("e: ", e);
        }

        return true;
    }

}

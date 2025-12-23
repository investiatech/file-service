package tech.investia.file_service.services;

import org.springframework.stereotype.Service;
import tech.investia.file_service.models.FileResource;
import tech.investia.file_service.repositories.FileResourceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FileResourceService {
    private final FileResourceRepository fileResourceRepository;
    private final MinioStorageService minioStorageService;

    public FileResourceService(FileResourceRepository fileResourceRepository, MinioStorageService minioStorageService) {
        this.fileResourceRepository = fileResourceRepository;
        this.minioStorageService = minioStorageService;
    }

    //TODO mapper FileResource -> ResourceResponse
    public List<FileResource> findAll() {

        return fileResourceRepository.findAll();
    }

    public Optional<FileResource> findById(String id) {

        return fileResourceRepository.findById(id);
    }

    public boolean deleteById(String id) {
        return fileResourceRepository.findById(id)
                .map(fileResource -> {
                    try {
                        minioStorageService.deleteObject(fileResource.getBucket(), fileResource.getObjectKey());
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to delete object from minio: " + fileResource.getObjectKey() + e);
                    }
                    fileResourceRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }

}

package tech.investia.file_service.controllers;

import io.minio.GetObjectResponse;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import org.intellij.lang.annotations.RegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import tech.investia.file_service.dto.FileUploadRequest;
import tech.investia.file_service.dto.Language;
import tech.investia.file_service.dto.ResourceResponse;
import tech.investia.file_service.mappers.ResourceMapper;
import tech.investia.file_service.models.FileResource;
import tech.investia.file_service.services.FileIngestService;
import tech.investia.file_service.services.FileResourceService;
import tech.investia.file_service.services.MinioStorageService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/resource/files")
public class FileResourceController {
    private static final Logger log = LoggerFactory.getLogger(FileResourceController.class);
    private final FileIngestService fileIngestService;
    private final MinioStorageService minioStorageService;
    private final FileResourceService fileResourceService;
    private final ResourceMapper resourceMapper;

    public FileResourceController(FileIngestService fileIngestService, MinioStorageService minioStorageService, FileResourceService fileResourceService, ResourceMapper resourceMapper) {
        this.fileIngestService = fileIngestService;
        this.minioStorageService = minioStorageService;
        this.fileResourceService = fileResourceService;
        this.resourceMapper = resourceMapper;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadOne(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute FileUploadRequest meta,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) throws Exception {
        try {
            String name = (meta.name() != null && !meta.name().isBlank())
                    ? meta.name()
                    : file.getOriginalFilename();

            ResourceResponse response = fileIngestService.uploadAndRecord(file, name, meta.overwrite());
//            fileIngestService.triggerIngest(response.id(), authorization);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Object already exists"));
        }
    }

    @GetMapping("/list")
    public List<ResourceResponse> listAll() {
        return fileResourceService.findAll()
                .stream()
                .map(resourceMapper::toResourceResponse)
                .collect(Collectors.toList());
    }

//    @GetMapping("/pagination")
//    public ResponseEntity<Page<FileResource>> listAll(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt,desc") String sort
//    ) {
//        String[] sortParams = sort.split(",");
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));
//        return ResponseEntity.ok(fileResourceService.findAll(pageable));
//    }

    @GetMapping("/download/{id}")
    public ResponseEntity<StreamingResponseBody> downloadById(@PathVariable String id) throws Exception {
        FileResource fileResource = fileResourceService.findById(id).orElse(null);
        if (fileResource == null) return ResponseEntity.notFound().build();
        StatObjectResponse statObjectResponse = minioStorageService.statObjectResponse(fileResource.getBucket(), fileResource.getObjectKey());
        String contentType = Optional.ofNullable(fileResource.getContentType()).orElse(Optional.ofNullable(statObjectResponse.contentType()).orElse("application/octet-stream"));
        long size = statObjectResponse.size();
        GetObjectResponse objectStream = minioStorageService.getObjectResponse(fileResource.getBucket(), fileResource.getObjectKey());
        StreamingResponseBody streamingResponseBody = outputStream -> {
            try (objectStream) {
                objectStream.transferTo(outputStream);
            }
            ;
        };
        String filename = Optional.ofNullable(fileResource.getName()).orElse(fileResource.getObjectKey());
        String contentDisposition = contentDisposition(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).contentType(MediaType.parseMediaType(contentType)).contentLength(size).body(streamingResponseBody);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        boolean deleted = fileResourceService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private String contentDisposition(String filename) {
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        return "attachment; filename=\"" + filename.replace("\"", "") + "\"; filename*=UTF-8''" + encoded;
    }

}

package tech.investia.file_service.services;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.investia.file_service.storage.UploadResult;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class MinioStorageService {
    private final MinioClient minioClient;
    private final String bucket;

    public MinioStorageService(MinioClient minioClient, @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    public UploadResult upload(MultipartFile file, String objectName, boolean overwrite) throws Exception {
        ensureBucket();

        String key = Optional.ofNullable(objectName)
                .filter(s -> !s.isBlank())
                .orElse(UUID.randomUUID() + "-" + file.getOriginalFilename());

//        String key = UUID.randomUUID().toString();

//        if (key == null || key.isBlank()) throw new IllegalArgumentException("Missing object name.");
        if (!overwrite && objectExists(key))
            throw new FileAlreadyExistsException(key);

        String contentType = Optional.ofNullable(file.getContentType()).orElse("application/octet-stream");

        try (InputStream inputStream = file.getInputStream()) {
            {
                ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(contentType)
                        .build());
                return new UploadResult(key, objectWriteResponse.etag(), file.getSize(), contentType);
            }
        }
    }

    private boolean objectExists(String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucket)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureBucket() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        }
    }

    public StatObjectResponse statObjectResponse(String bucket, String objectKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build());
    }

    public GetObjectResponse getObjectResponse(String bucket, String objectKey) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build());
    }

    public void deleteObject(String bucket, String objectKey) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build());
    }

    public String bucket() {
        return bucket;
    }

    public void uploadText(String objectName, String content, String contentType) throws Exception {
        ensureBucket();
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, bytes.length, -1)
                    .contentType(contentType)
                    .build()
            );
        }
    }

}
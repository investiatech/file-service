package tech.investia.file_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.investia.file_service.models.FileResource;

@Repository
public interface FileResourceRepository extends MongoRepository<FileResource, String> {

}

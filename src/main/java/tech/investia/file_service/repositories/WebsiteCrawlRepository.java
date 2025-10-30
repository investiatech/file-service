package tech.investia.file_service.repositories;

import tech.investia.file_service.models.WebsiteCrawl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteCrawlRepository extends MongoRepository<WebsiteCrawl, String> {

}

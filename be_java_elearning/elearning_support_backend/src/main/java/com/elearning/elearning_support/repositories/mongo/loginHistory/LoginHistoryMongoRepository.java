package com.elearning.elearning_support.repositories.mongo.loginHistory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.elearning.elearning_support.models.mongo.LoginHistoryDocument;

@Repository
public interface LoginHistoryMongoRepository extends MongoRepository<LoginHistoryDocument, String> {

}

package tcpserver.handler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyValueMongoRepository extends MongoRepository<KeyValue,Integer> {
    KeyValue findByKey(String key);
}

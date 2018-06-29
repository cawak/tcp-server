package tcpserver.handler.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import tcpserver.handler.repository.cache.KeyValueCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
class KeyValueRepositoryImpl implements KeyValueRepository {

    @Autowired
    private KeyValueCache keyValueCache;

    @Autowired
    private KeyValueMongoRepository keyValueMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ReadWriteLock lock;

    public KeyValueRepositoryImpl() {
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public void setByKey(String key, List<String> values){
        lock.writeLock().lock();

        try {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(key);
            keyValue.setValues(values);
            keyValueMongoRepository.save(keyValue);
            keyValueCache.set(key, ValueType.KEY, values);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<String> getByKey(String key){

        Function<String, List<String>> dbQuery =
                (String key1) -> {
                    KeyValue keyValue = keyValueMongoRepository.findByKey(key1);
                    List<String> values;
                    if (keyValue == null || keyValue.getValues() == null) {
                        values = new ArrayList<>();
                    } else {
                        values = keyValue.getValues();
                        keyValueCache.set(keyValue.getKey(), ValueType.KEY, keyValue.getValues());
                    }
                    return values;
                };

        return get(key, ValueType.KEY, dbQuery);
    }

    @Override
    public List<String> getAllKeysByPattern(String pattern){
        Function<String, List<String>> dbQuery =
                (String key) -> {
                    List<String> keys;
                    Query query = new Query();
                    query.addCriteria(Criteria.where("key").regex(pattern));
                    query.fields().include("key");
                    List<KeyValue> keyValue = mongoTemplate.find(query, KeyValue.class);
                    if (keyValue == null) {
                        keys = new ArrayList<>();
                    } else {
                        keys = keyValue.stream().map(KeyValue::getKey).collect(Collectors.toList());
                        keyValueCache.set(pattern, ValueType.PATTERN, keys);
                    }
                    return keys;
                };

        return get(pattern, ValueType.PATTERN, dbQuery);
    }

    private List<String> get(String key, ValueType valueType, Function<String,List<String>> dbQuery){
        lock.readLock().lock();
        boolean isReadLockReleased = false;
        List<String> keys;
        try{
            keys = keyValueCache.getByKeyAndType(key, valueType);
            if (keys == null) {
                lock.readLock().unlock();
                isReadLockReleased = true;
                lock.writeLock().lock();
                try {
                    keys = dbQuery.apply(key);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        } finally {
            if (!isReadLockReleased) {
                lock.readLock().unlock();
            }
        }

        return keys;
    }


}

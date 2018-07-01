package tcpserver.handler.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import tcpserver.handler.repository.cache.KeyValueCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class KeyValueRepositoryImpl implements KeyValueRepository {

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
        return get(key);
    }

    @Override
    public List<String> getAllKeysByPattern(String pattern){
        Function<String, List<String>> dbQuery =
                (String key) -> {
                    List<String> keys;
                    Query query = new Query();
                    query.addCriteria(Criteria.where("key").regex(pattern, "ims"));
                    query.fields().include("key");
                    List<KeyValue> keyValue = mongoTemplate.find(query, KeyValue.class);
                    if (keyValue == null || keyValue.isEmpty()) {
                        keys = new ArrayList<>();
                    } else {
                        keys = keyValue.stream().map(KeyValue::getKey).collect(Collectors.toList());
                        keyValueCache.set(pattern, ValueType.PATTERN, keys);
                    }
                    return keys;
                };

        return get(pattern, ValueType.PATTERN, dbQuery);
    }

    @Override
    public void addValueToKeyFromRight(String key, String value){
        lock.writeLock().lock();

        try{
            mongoTemplate.upsert(
                    Query.query(Criteria.where("key").is(key)),
                    new Update().push("values", value),
                    KeyValue.class
            );
            if (this.keyValueCache.getByKeyAndType(key, ValueType.KEY) != null){
                keyValueCache.addValueToKeyFromRight(key, value);
            } else {
                this.getKeyQuery().apply(key);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addValueToKeyFromLeft(String key, String value){
        lock.writeLock().lock();

        try{
            mongoTemplate.upsert(
                    Query.query(Criteria.where("key").is(key)),
                    new Update().push("values").atPosition(0).value(value),
                    KeyValue.class
            );
            if (this.keyValueCache.getByKeyAndType(key, ValueType.KEY) != null){
                keyValueCache.addValueToKeyFromLeft(key, value);
            } else {
                this.getKeyQuery().apply(key);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Function<String, List<String>> getKeyQuery(){
        return (String key1) -> {
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
    }

    //There is a bug here. Take out the function as a getter and use it in the adders without releasing the write lock
    //as in the get under this get.
    private List<String> get(String key){
        return get(key, ValueType.KEY, getKeyQuery());
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

package tcpserver.handler.repository.cache;


import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.ValueType;

import java.util.*;

@Component
public class KeyValueCacheImpl implements KeyValueCache {

    private final static int CACHE_CAPACITY = 10;

    private final OrderedMap<CacheKey, List<String>> cacheContainer = new LinkedMap<>();
    private final Set<String> patterns = new HashSet<>();

    public KeyValueCacheImpl() {
    }

    @Override
    public void set(String key, ValueType valueType, List<String> values){
        this.removeLruIfCacheIsFull();
        CacheKey cacheKeyToAdd = new CacheKey(key, valueType);
        cacheContainer.put(new CacheKey(key, valueType), values);
        if (cacheKeyToAdd.isPattern()){
            patterns.add(key);
        }
    }

    public void addKeyToExistingPattern(String key){
        /*
            something with patterns set - iterate over it and for each matching pattern add the value to the same
            key-pattern in cache to its list.
         */
    }

    @Override
    public void addValueTo(String key, ValueType valueType, String value){
        CacheKey cacheKey = new CacheKey(key, valueType);

        List<String> values = cacheContainer.get(cacheKey);
        if (values == null){
            this.removeLruIfCacheIsFull();
            values = new ArrayList<>(Collections.singleton(value));
            cacheContainer.put(cacheKey, values);
        } else {
            values.add(value);
        }
    }

    private void removeLruIfCacheIsFull(){
        CacheKey removedCacheKey = null;
        if (cacheContainer.size() >= CACHE_CAPACITY) {
            CacheKey lastUsedCacheKey = cacheContainer.lastKey();
            cacheContainer.remove(lastUsedCacheKey);
            removedCacheKey = lastUsedCacheKey;
        }
        if (removedCacheKey != null && removedCacheKey.getKey() != null && removedCacheKey.isPattern()){
            patterns.remove(removedCacheKey.getKey());
        }
    }

    @Override
    public List<String> getByKeyAndType(String key, ValueType valueType){
        CacheKey cacheKey = new CacheKey(key, valueType);
        return cacheContainer.get(cacheKey);
    }






}

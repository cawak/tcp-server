package tcpserver.handler.repository.cache;


import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.ValueType;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class KeyValueCacheImpl implements KeyValueCache {

    private final static int CACHE_CAPACITY = 10;

    private final OrderedMap<CacheKey, List<String>> cacheContainer = new LinkedMap<>();
    private final Set<String> patterns = new HashSet<>();

    private enum Direction {
        LEFT(true),
        RIGHT(false);

        private boolean isLeft;

        Direction(boolean isLeft){
            this.isLeft = isLeft;
        }

        public boolean isLeft(){
            return isLeft;
        }
    }

    public KeyValueCacheImpl() {
    }

    @Override
    public void set(String key, ValueType valueType, List<String> values){
        this.removeLruIfCacheIsFull();
        CacheKey cacheKeyToAdd = new CacheKey(key, valueType);
        cacheContainer.put(new CacheKey(key, valueType), values);
        if (cacheKeyToAdd.isPattern()){
            patterns.add(key);
        } else {
            addKeyToExistingPattern(key);
        }
    }

    /*public void setPattern(String patter, List<String> values){

    }*/

    @Override
    public void addValueToKeyFromRight(String key, String value){
        addValueTo(key, value, Direction.RIGHT);
    }

    @Override
    public void addValueToKeyFromLeft(String key, String value){
        addValueTo(key, value, Direction.LEFT);
    }

    private void addKeyToExistingPattern(String key){
        Set<String> matchingPatterns = patterns.stream().filter(pattern -> {
            Pattern p = Pattern.compile(pattern);
            return p.matcher(key).matches();
        }).collect(Collectors.toSet());

        matchingPatterns.forEach(matchingPattern -> {
            addValueToPattern(matchingPattern, ValueType.PATTERN, key);
        });
    }

    private void addValueToPattern(String pattern, ValueType valueType, String key){
        CacheKey cacheKey = new CacheKey(pattern, valueType);

        List<String> values = cacheContainer.get(cacheKey);
        if (values != null && !values.contains(key)){
            values.add(key);
        }
    }

    private void addValueTo(String key, String value, Direction direction){
        CacheKey cacheKey = new CacheKey(key, ValueType.KEY);

        List<String> values = cacheContainer.get(cacheKey);
        if (values == null){
            this.removeLruIfCacheIsFull();
            values = new ArrayList<>(Collections.singleton(value));
            cacheContainer.put(cacheKey, values);
        } else {
            if (direction.isLeft()){
                values.add(0,value);
            } else {
                values.add(value);
            }
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

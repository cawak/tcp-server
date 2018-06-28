package tcpserver.handler.repository.cache;


import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.LinkedMap;
import tcpserver.handler.repository.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class KeyValueCache {

    private final static int CACHE_CAPACITY = 10;

    private final OrderedMap<CacheKey, List<String>> cacheContainer = new LinkedMap<>();

    public KeyValueCache() {
    }

    public void addValueTo(String key, ValueType valueType, String value){
        CacheKey cacheKey = new CacheKey(key, valueType);

        List<String> values = cacheContainer.get(cacheKey);
        if (values == null){
            values = new ArrayList<>(Collections.singleton(value));
            cacheContainer.put(cacheKey, values);
        } else {
            values.add(value);
        }
    }

}

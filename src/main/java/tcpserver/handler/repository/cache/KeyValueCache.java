package tcpserver.handler.repository.cache;

import tcpserver.handler.repository.ValueType;

import java.util.List;

public interface KeyValueCache {

    void set(String key, ValueType valueType, List<String> values);
    void addValueTo(String key, ValueType valueType, String value);
    List<String> getByKeyAndType(String key, ValueType valueType);

}

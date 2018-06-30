package tcpserver.handler.repository;

import java.util.List;

public interface KeyValueRepository {

    void setByKey(String key, List<String> values);
    List<String> getByKey(String key);
    List<String> getAllKeysByPattern(String pattern);
    void addValueToKeyFromRight(String key, String value);
    void addValueToKeyFromLeft(String key, String value);

}

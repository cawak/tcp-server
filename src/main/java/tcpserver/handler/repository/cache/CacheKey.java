package tcpserver.handler.repository.cache;

import tcpserver.handler.repository.ValueType;

public class CacheKey {

    private String key;
    private ValueType valueType;

    public CacheKey() {
    }

    public CacheKey(String key, ValueType valueType) {
        this.key = key;
        this.valueType = valueType;
    }

    public String getKey() {
        return key;
    }

    public boolean isPattern(){
        return ValueType.PATTERN == this.valueType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (key != null ? !key.equals(cacheKey.key) : cacheKey.key != null) return false;
        return valueType == cacheKey.valueType;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
        return result;
    }
}

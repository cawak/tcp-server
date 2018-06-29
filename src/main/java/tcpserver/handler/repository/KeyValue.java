package tcpserver.handler.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
class KeyValue {

    @Id
    private String key;

    private List<String> values;

    public KeyValue() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }


    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", values=" + values +
                '}';
    }
}

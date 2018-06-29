package tcpserver.handler.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

@Component
class GetHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public void handle(String key, DataOutputStream dataOutputStream) throws IOException {
        if (key == null || key.isEmpty()){
            System.out.println("Got wrong get command arguments.");
            dataOutputStream.writeUTF("Got wrong get command arguments.");
        } else {
            List<String> value = keyValueRepository.getByKey(key);
            dataOutputStream.writeUTF(value.toString() + System.lineSeparator());
            dataOutputStream.flush();
        }
    }

    @Override
    public String getApi() {
        return "get";
    }
}

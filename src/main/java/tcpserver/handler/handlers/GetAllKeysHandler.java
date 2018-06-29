package tcpserver.handler.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

@Component
class GetAllKeysHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public void handle(String pattern, DataOutputStream dataOutputStream) throws IOException {
        List<String> keys = keyValueRepository.getAllKeysByPattern(pattern);
        dataOutputStream.writeUTF(keys.toString() + System.lineSeparator());
    }

    @Override
    public String getApi() {
        return "getAllKeys";
    }
}

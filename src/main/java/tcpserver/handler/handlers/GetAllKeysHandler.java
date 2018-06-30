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

    @Autowired
    private ErrorHandler errorHandler;

    @Override
    public void handle(String pattern, DataOutputStream dataOutputStream) throws IOException {
        String errorMessage = "getAllKeys: Wrong string pattern!";
        if (!pattern.startsWith("^") || !pattern.endsWith("$")){
            errorHandler.handle(dataOutputStream, errorMessage);
            return;
        }
        try {
            List<String> keys = keyValueRepository.getAllKeysByPattern(pattern);
            dataOutputStream.writeUTF(keys.toString() + System.lineSeparator());
        } catch (Exception e){
            errorHandler.handle(dataOutputStream, errorMessage);
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.GetAllKeys;
    }
}

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

    @Autowired
    private ErrorHandler errorHandler;

    @Override
    public void handle(String key, DataOutputStream dataOutputStream) throws IOException {
        if (key == null || key.isEmpty()){
            handleError(dataOutputStream);
        } else {
            try {
                List<String> value = keyValueRepository.getByKey(key);
                dataOutputStream.writeUTF(value.toString() + System.lineSeparator());
                dataOutputStream.flush();
            } catch (IOException e) {
                handleError(dataOutputStream);
            }
        }
    }

    private void handleError(DataOutputStream dataOutputStream){
        errorHandler.handle(dataOutputStream, "Got wrong get command arguments." + System.lineSeparator());
    }

    @Override
    public HandlerType getType() {
        return HandlerType.Get;
    }
}

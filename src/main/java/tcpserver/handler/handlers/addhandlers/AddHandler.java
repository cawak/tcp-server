package tcpserver.handler.handlers.addhandlers;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.handlers.ErrorHandler;
import tcpserver.handler.handlers.HandlerType;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
public class AddHandler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Autowired
    private ErrorHandler errorHandler;

    public void handle(String data, DataOutputStream dataOutputStream, HandlerType handlerType) throws IOException {
        String[] str = data.split(" ");
        if (str.length != 2){
            errorHandler.handle(dataOutputStream, handlerType.getType() + ": wrong arguments");
            return;
        }
        Pair<String,String> pair = Pair.of(str[0], str[1]);
        if (handlerType.equals(HandlerType.RightAdd)){
            keyValueRepository.addValueToKeyFromRight(pair.getLeft(), pair.getRight());
        } else {
            keyValueRepository.addValueToKeyFromLeft(pair.getLeft(), pair.getRight());
        }
        dataOutputStream.writeUTF("Value was added to list." + System.lineSeparator());

    }
}

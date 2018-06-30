package tcpserver.handler.handlers.addhandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.handlers.Handler;
import tcpserver.handler.handlers.HandlerType;
import java.io.DataOutputStream;
import java.io.IOException;

@Component
class LeftAddHandler implements Handler {

    @Autowired
    private AddHandler addHandler;

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {
        addHandler.handle(data, dataOutputStream, getAddType());
    }

    @Override
    public HandlerType getType() {
        return getAddType();
    }

    private HandlerType getAddType(){
        return HandlerType.LeftAdd;
    }
}

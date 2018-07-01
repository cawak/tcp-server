package tcpserver.handler.handlers;

import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
class DefaultHandler implements Handler {

    @Override
    public String handle(String data) throws IOException{
        return "Sorry, command " + data + " is not supported";
    }

    @Override
    public HandlerType getType() {
        return HandlerType.Default;
    }
}

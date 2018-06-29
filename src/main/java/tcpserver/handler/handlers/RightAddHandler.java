package tcpserver.handler.handlers;


import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
class RightAddHandler implements Handler {

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public String getApi() {
        return "rightAdd";
    }
}

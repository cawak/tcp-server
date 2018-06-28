package tcpserver.handler.handlers;

import java.io.DataOutputStream;
import java.io.IOException;

public class LeftAddHandler implements Handler {

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public String getApi() {
        return "leftAdd";
    }
}

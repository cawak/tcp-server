package tcpserver.handler.handlers;


import java.io.DataOutputStream;
import java.io.IOException;

public class RightAddHandler implements Handler {

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public String getApi() {
        return "rightAdd";
    }
}

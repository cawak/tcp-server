package tcpserver.handler.handlers;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Handler {

    void handle(String data, DataOutputStream dataOutputStream) throws IOException;
    String getApi();
}

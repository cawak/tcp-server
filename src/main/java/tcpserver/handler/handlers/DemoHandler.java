package tcpserver.handler.handlers;

import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
public class DemoHandler implements Handler {

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException{
        dataOutputStream.writeUTF("Sorry, command " + data + " is not supported" + System.lineSeparator());
        dataOutputStream.flush();
    }

    @Override
    public String getApi() {
        return "Default";
    }
}

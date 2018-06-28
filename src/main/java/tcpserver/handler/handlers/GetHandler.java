package tcpserver.handler.handlers;

import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;

@Component
public class GetHandler implements Handler {

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public String getApi() {
        return "get";
    }
}

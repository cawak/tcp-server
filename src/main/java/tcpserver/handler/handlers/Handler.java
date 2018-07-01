package tcpserver.handler.handlers;

import java.io.IOException;

public interface Handler {

    String handle(String data) throws IOException;
    HandlerType getType();
}

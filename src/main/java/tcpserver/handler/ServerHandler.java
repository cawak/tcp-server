package tcpserver.handler;


import java.net.Socket;

public interface ServerHandler {

    void handle(Socket server);

}

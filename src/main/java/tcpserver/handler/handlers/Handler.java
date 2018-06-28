package tcpserver.handler.handlers;

public interface Handler {

    void handle(String data);
    String getApi();
}

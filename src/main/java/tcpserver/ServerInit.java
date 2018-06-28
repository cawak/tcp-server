package tcpserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.ServerHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class ServerInit {

    @Autowired
    private ServerHandler serverHandler;

    public void startServer() throws Exception {
            try(ServerSocket serverSocket = new ServerSocket(12345)) {
                while (true) {
                    try {
                        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                        Socket server = serverSocket.accept();
                        server.setSoTimeout(60_000);
                        serverHandler.handle(server);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    }

}

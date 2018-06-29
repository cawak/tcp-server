package tcpserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tcpserver.handler.handlers.HandlerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

@Component
public class ServerHandlerImpl implements ServerHandler {

    @Autowired
    private HandlerFactory handlerFactory;

    @Override
    @Async("tcpServerHandler")
    public void handle(Socket server){
        System.out.println(LocalDateTime.now() + ": " + Thread.currentThread().getName() + " handling client " + server.getRemoteSocketAddress());

        try (   OutputStream outputStream = server.getOutputStream();
                DataOutputStream toClient = new DataOutputStream(outputStream);
                InputStream inputStream = server.getInputStream();
                DataInputStream fromClient = new DataInputStream(inputStream)
        ) {
            while (server.isConnected()) {
                String line = fromClient.readUTF();

                int indexOfCommand = line.trim().indexOf(" ");
                String command = line.trim();
                String lineToHandle = command;
                if (indexOfCommand > 0){
                    command = line.substring(0, indexOfCommand);
                    lineToHandle = line.substring(indexOfCommand).trim();
                }

                handlerFactory.getHandler(command).handle(lineToHandle, toClient);
                outputStream.flush();

            }
        } catch (Exception e) {
            System.out.println("Got an exception in handler " + Thread.currentThread().getName());
        }
    }

}

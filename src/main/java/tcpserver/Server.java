package tcpserver;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {

    public void startServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            while (true) {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                PrintWriter toClient =
                        new PrintWriter(server.getOutputStream(), true);
                BufferedReader fromClient =
                        new BufferedReader(
                                new InputStreamReader(server.getInputStream()));
                String line = fromClient.readLine();
                System.out.println("Server received: " + line);
                toClient.println("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

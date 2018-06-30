package tcpserver.handler.handlers;

import org.springframework.stereotype.Component;

import java.io.DataOutputStream;

@Component
public class ErrorHandler {

    public ErrorHandler(){

    }

    public void handle(DataOutputStream dataOutputStream, String errorMessage){
        try {
            System.out.println(errorMessage);
            dataOutputStream.writeUTF(errorMessage);
            dataOutputStream.flush();
        } catch (Exception e){
            System.out.println("Got an exception while processing output stream to client." + System.lineSeparator());
        }
    }
}

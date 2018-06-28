package tcpserver.handler.handlers;

import org.springframework.stereotype.Component;

@Component
public class DemoHandler implements Handler {

    @Override
    public void handle(String data) {
        System.out.println("DemoHandler got a data: " + data);
    }

    @Override
    public String getApi() {
        return "Demo";
    }
}

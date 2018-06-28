package tcpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TcpServer {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(TcpServer.class, args);

        ServerInit server = context.getBean(ServerInit.class);

        try {
            server.startServer();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}

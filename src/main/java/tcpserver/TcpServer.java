package tcpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TcpServer {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(TcpServer.class, args);

        Server server = context.getBean(Server.class);

        try {
            server.startServer();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            context.close();
        }
    }

    /*public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(TcpServer.class, args);
        Socket socket = SocketFactory.getDefault().createSocket("localhost", 1234);
        socket.getOutputStream().write("foo\r\n".getBytes());
        socket.close();
        Thread.sleep(1000);
        context.close();
    }

    @Bean
    public TcpReceivingChannelAdapter inbound(AbstractServerConnectionFactory cf) {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(cf);
        adapter.setOutputChannel(tcpIn());
        return adapter;
    }
        @Bean
        public MessageChannel tcpIn() {
            return new DirectChannel();
        }

        @Transformer(inputChannel = "tcpIn", outputChannel = "serviceChannel")
        @Bean
        public ObjectToStringTransformer transformer() {
            return new ObjectToStringTransformer();
        }


        @ServiceActivator(inputChannel = "serviceChannel")
        public void service(String in) {
            System.out.println(in);
        }

    @Bean
    public TcpNetServerConnectionFactory cf() {
        return new TcpNetServerConnectionFactory(1234);
    }*/
}

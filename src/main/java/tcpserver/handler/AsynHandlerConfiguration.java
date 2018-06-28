package tcpserver.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsynHandlerConfiguration {

    @Bean(name = "tcpServerHandler")
    public Executor tcpServerHandler(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(60);
        executor.setDaemon(false);
        executor.setThreadNamePrefix("TcpServerHandler-");
        executor.initialize();
        return executor;
    }

}

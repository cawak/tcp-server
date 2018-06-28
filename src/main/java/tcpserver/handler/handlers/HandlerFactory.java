package tcpserver.handler.handlers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class HandlerFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Map<String, Handler> requestHandlers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        requestHandlers = new HashMap<>();
        Collection<Handler> handlerBeans = applicationContext.getBeansOfType(Handler.class).values();
        handlerBeans.forEach(bean -> requestHandlers.put(bean.getApi(), bean));
    }

    public Handler getHandler(String type){
        Handler handler = requestHandlers.get(type);

        if (handler == null){
            handler = requestHandlers.get("Default");
        }

        return handler;
    }
}

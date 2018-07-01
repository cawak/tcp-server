package tcpserver.handler.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.util.List;

@Component
class GetHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public String handle(String key) {
        String message = null;
        if (key == null || key.isEmpty()){
            message = "Got wrong get command arguments.";
        } else {
            try {
                List<String> values = keyValueRepository.getByKey(key);
                message = values.toString();
            } catch (Exception e) {
                System.out.println("Got an exception while processing get. " + e);
                message = "Something is wrong with the get arguments.";
            }
        }

        return message;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.Get;
    }
}

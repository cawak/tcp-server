package tcpserver.handler.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

@Component
class GetAllKeysHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public String handle(String pattern) throws IOException {
        String returnMessage;
        if (!pattern.startsWith("^") || !pattern.endsWith("$")){
            returnMessage = "getAllKeys: Wrong string pattern!";
        } else {
            try {
                List<String> keys = keyValueRepository.getAllKeysByPattern(pattern);
                returnMessage = keys.toString();
            } catch (Exception e) {
                System.out.println("Got an exception while processing getAllKeys: " + e);
                returnMessage = "Something is wrong with the arguments.";
            }
        }
        return returnMessage;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.GetAllKeys;
    }
}

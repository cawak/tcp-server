package tcpserver.handler.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;

public class GetAllKeysHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public void handle(String pattern, DataOutputStream dataOutputStream) {

    }

    private String getAllKeys(String pattern){
        return "";
    }

    @Override
    public String getApi() {
        return "getAllKeys";
    }
}

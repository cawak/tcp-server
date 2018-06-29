package tcpserver.handler.handlers;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class SetHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {
        if (data == null || !data.contains(" ")){
            this.notifyClientOfBadCommand(dataOutputStream);
        } else {
            int splitIndex = data.indexOf(" ");
            Pair<String, String> command = Pair.of(data.substring(0, splitIndex), data.substring(splitIndex));
            String key = command.getLeft();
            List<String> values = new ArrayList<>(Arrays.asList(command.getRight().split(" ")));
            values.remove(0);
            this.storeInDb(key, values);
            dataOutputStream.writeUTF("Value with key: " + key + " was successfully set." + System.lineSeparator());
            dataOutputStream.flush();
        }
    }

    @Override
    public String getApi() {
        return "set";
    }

    private void notifyClientOfBadCommand(DataOutputStream dataOutputStream) throws IOException {
        String errMsg = "Got empty or wrong format set command arguments";
        dataOutputStream.writeUTF(errMsg + System.lineSeparator());
        dataOutputStream.flush();
        System.out.println(errMsg);
    }

    private void storeInDb(String key, List<String> values){
        keyValueRepository.setByKey(key, values);
    }


}

package tcpserver.handler.handlers;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
class SetHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Autowired
    private ErrorHandler errorHandler;

    @Override
    public void handle(String data, DataOutputStream dataOutputStream) throws IOException {
        if (data == null || !data.contains(" ")){
            this.errorHandler(dataOutputStream);
        } else {
            try {
                int splitIndex = data.indexOf(" ");
                Pair<String, String> command = Pair.of(data.substring(0, splitIndex), data.substring(splitIndex + 1));
                String key = command.getLeft();
                List<String> values = parseStringToList(command.getRight(), dataOutputStream);
                this.storeInDb(key, values);
                dataOutputStream.writeUTF("Value with key: " + key + " was successfully set." + System.lineSeparator());
                dataOutputStream.flush();
            } catch (IOException e) {
                this.errorHandler(dataOutputStream);
            }
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.Set;
    }

    private List<String> parseStringToList(String string, DataOutputStream dataOutputStream) throws IOException {
        if (!string.startsWith("[") || !string.endsWith("]")){
            this.errorHandler(dataOutputStream);
        }

        String formattedList = string.substring(1, string.length()-1);
        List<String> list = new LinkedList<>();
        if (!formattedList.isEmpty()){
            list = Arrays
                    .stream(formattedList.split(","))
                    .map(String::trim)
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        return list;
    }

    private void errorHandler(DataOutputStream dataOutputStream) throws IOException {
        String errMsg = "Got empty or wrong format set command arguments" + System.lineSeparator();
        errorHandler.handle(dataOutputStream, errMsg);
    }

    private void storeInDb(String key, List<String> values){
        keyValueRepository.setByKey(key, values);
    }


}

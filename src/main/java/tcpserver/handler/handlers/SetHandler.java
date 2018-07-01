package tcpserver.handler.handlers;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcpserver.handler.repository.KeyValueRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
class SetHandler implements Handler {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Override
    public String handle(String data) throws IOException {
        String message = null;
        if (data == null || !data.contains(" ")){
            message = "Got empty or wrong format set command arguments";
        } else {
            try {
                int splitIndex = data.indexOf(" ");
                Pair<String, String> command = Pair.of(data.substring(0, splitIndex), data.substring(splitIndex + 1));
                String key = command.getLeft();
                List<String> values = parseStringToList(command.getRight());
                this.storeInDb(key, values);
                message = "Value with key: " + key + " was successfully set.";
            } catch (Exception e) {
                System.out.println("Got an exception while processing set. " + e);
                message = "Something is wrong with the arguments.";
            }
        }

        return message;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.Set;
    }

    private List<String> parseStringToList(String string) throws ParseException {
        if (!string.startsWith("[") || !string.endsWith("]")){
            throw new ParseException("No colons at the beginning", 0);
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

    private void storeInDb(String key, List<String> values){
        keyValueRepository.setByKey(key, values);
    }


}

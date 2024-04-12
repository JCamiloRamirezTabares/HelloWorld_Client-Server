package commands;

import AppInterfaces.RequesterPrx;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class SendToCommand extends Command{

    private ConcurrentHashMap<String, RequesterPrx> map;

    public SendToCommand(ConcurrentHashMap<String, RequesterPrx> map){
        this.map = map;
    }

    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {

        String[] parts = s.split("to");
        String sender = parts[0].trim();
        String parts2[] = parts[1].trim().split(" ");
        String receiver = parts2[0];
        String message = String.join(" ", Arrays.copyOfRange(parts2, 1, parts2.length)).trim();


        if(map != null){
            RequesterPrx receiverPrx = map.get(receiver);

            if(receiverPrx != null){
                receiverPrx.printString(receiver + " say: " + message);
            } else{
                setOutput("The receiver is not registered");
            }
        } else{
            setOutput("The message could not be sent because there are no registered clients");
        }

    }
}

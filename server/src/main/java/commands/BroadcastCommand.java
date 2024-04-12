package commands;

import AppInterfaces.RequesterPrx;

import java.util.concurrent.ConcurrentHashMap;

public class BroadcastCommand extends Command{

    private ConcurrentHashMap<String, RequesterPrx> map;

    public BroadcastCommand(ConcurrentHashMap<String, RequesterPrx> map){
        this.map = map;
    }

    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        if(map != null){
            if(!s.isEmpty()){
                map.keySet().forEach((String client) -> {
                    map.get(client).printString(s);
                });

                setOutput("The message has been sent to all clients registered on the server");
            } else {
                setOutput("The message couldn't be sent because it is empty");
            }
        } else{
            setOutput("The message could not be sent because there are no registered clients");
        }
    }
}

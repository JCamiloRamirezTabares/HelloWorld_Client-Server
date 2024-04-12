package commands;

import AppInterfaces.RequesterPrx;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class ListClientsCommand extends Command{

    private ConcurrentHashMap<String, RequesterPrx> map;

    public ListClientsCommand(ConcurrentHashMap<String, RequesterPrx> map){
        this.map = map;
    }

    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        String clientsList = Arrays.toString(map.keySet().toArray());
        setOutput("Registered clients: " + clientsList);
    }
}

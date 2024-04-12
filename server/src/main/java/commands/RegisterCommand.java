package commands;

import AppInterfaces.RequesterPrx;

import java.util.concurrent.ConcurrentHashMap;

public class RegisterCommand extends Command{

    private ConcurrentHashMap<String, RequesterPrx> map;

    public RegisterCommand(ConcurrentHashMap<String, RequesterPrx> map){
        this.map = map;
    }

    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        if(map.get(s) == null){
            map.put(s, proxy);
            setOutput("The client has been successfully registered");
        } else{
            setOutput("Client is already registered");
        }
    }
}

package commands;

import AppInterfaces.RequesterPrx;

public class NonCommand extends Command{
    
    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        System.out.println("Command not found");
    }

    @Override
    public String getOutput() {
        return "Command not found";
    }
}

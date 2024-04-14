package commands;

import AppInterfaces.RequesterPrx;

public abstract class Command {

    protected long startTime;
    protected long endTime;

    protected String output;

    public Command(){
        startTime = 0;
        endTime = 0;
        output = "";
    }

    public void execute(String s, RequesterPrx proxy){
        startTime = System.nanoTime() / 1000;
        executeCommand(s, proxy);
        endTime = System.nanoTime() / 1000;
    }

    protected void executeCommand(String s, RequesterPrx proxy){}

    public void setOutput(String o){
        output = o;
    }

    public String getOutput(){
        return output;
    }

    public long getLatency(){
        return endTime - startTime;
    }
}
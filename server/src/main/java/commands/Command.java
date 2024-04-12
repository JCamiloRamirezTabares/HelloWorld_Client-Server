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
        startTime = System.currentTimeMillis();
        executeCommand(s, proxy);
        endTime = System.currentTimeMillis();
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
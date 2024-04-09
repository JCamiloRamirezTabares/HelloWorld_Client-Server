package commands;

public abstract class Command {

    protected long startTime;
    protected long endTime;

    protected String output;

    public Command(){
        startTime = 0;
        endTime = 0;
        output = "";
    }

    public void execute(String s){
        startTime = System.currentTimeMillis();
        excecuteCommand(s);
        endTime = System.currentTimeMillis();
    }

    protected void excecuteCommand(String s){}

    public void setOutput(String o){
        output = o;
    }

    public String getOutput(){
        return output;
    }
}
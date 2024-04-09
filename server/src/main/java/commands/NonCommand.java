package commands;

public class NonCommand extends Command{
    
    @Override
    public void excecuteCommand(String s) {
        System.out.println("Command not found");
    }
}

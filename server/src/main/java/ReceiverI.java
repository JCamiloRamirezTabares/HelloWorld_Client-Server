import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import AppInterfaces.Receiver;
import AppInterfaces.RequesterPrx;
import com.zeroc.Ice.Current;
import commands.Command;
import commands.ExecuteCommand;
import commands.FibonacciComand;
import commands.ListIfsCommand;
import commands.ListPortsCommand;
import commands.NonCommand;

public class ReceiverI implements Receiver
{

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public int timeout = 0;
    private int totalRequests;
    private int unprocessed;

    public ReceiverI(){
        totalRequests = 0;
        unprocessed = 0;
    }

    @Override
    public String printString(RequesterPrx requester, String s, Current current) {
        System.out.println(s);
        String[] parts = s.split(" ");

        StringBuilder res = new StringBuilder();
        res.append("Server Response to " + parts[0] + " ");

        try {
            totalRequests++;
            res.append(evaluateString(parts[1]));
        } catch (com.zeroc.Ice.ConnectionTimeoutException e) {
            timeout++;
            unprocessed++;
            res.append("Timeout: Request not completed within the specified time.");
        } catch (Exception e) {
            unprocessed++;
            res.append("Error processing request: " + e.getMessage());
        }

        System.out.println("Total requests sent: " + totalRequests);

        return res.toString();
    }

    public String evaluateString(String s){
        String request = s;
        Command command = new NonCommand();

        if(s != null){
            
            try {
                command = new FibonacciComand();
            } catch (Exception e) {}
    
            if(s.equalsIgnoreCase("listifs")){
                command = new ListIfsCommand();
            }
    
            if (s.startsWith("listports")) {
                request = s.substring("listports".length()).trim();
                command = new ListPortsCommand();
            }
    
            if (s.startsWith("!")) {
                request = s.substring(1).trim();
                command = new ExecuteCommand();
            }


            command.execute(request);

        } else{
            return "Null String can't processed";
        }

        return command.getOutput();
    }

    public void stopExecutorService() {
        executorService.shutdown();
    }
}
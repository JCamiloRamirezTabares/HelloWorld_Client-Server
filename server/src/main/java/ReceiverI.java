import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import AppInterfaces.Receiver;
import AppInterfaces.RequesterPrx;
import com.zeroc.Ice.Current;
import commands.*;

public class ReceiverI implements Receiver
{
    private final ConcurrentHashMap<String, RequesterPrx> clients;
    private final ExecutorService executorService;
    public int timeout = 0;
    private int totalRequests;
    private int unprocessed;

    public ReceiverI(){
        totalRequests = 0;
        unprocessed = 0;

        clients = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public String printString(RequesterPrx requester, String s, Current current) {
        System.out.println(s);
        totalRequests++;

        return processRequest(s, requester);
    }

    private String processRequest(String s, RequesterPrx proxy){

        Runnable runnable = () -> {
            String response = constructResponse(s, proxy);
            proxy.printString(response);
        };

        executorService.submit(runnable);

        System.out.println("Total requests received: " + totalRequests);

        return "Your response is being processed";
    }

    private String constructResponse(String request, RequesterPrx proxy){

        StringBuilder res = new StringBuilder();
        Command command = new NonCommand();
        String[] parts = request.split(":");

        String hostAndUserName = parts[0];
        String requestString = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)).trim();
        ;

        if(requestString != null){
            try {
                command = new FibonacciComand(requestString);
                res.append("Server response to " + hostAndUserName + ": ");
            } catch (Exception e) {}

            if(requestString.equalsIgnoreCase("listifs")){
                command = new ListIfsCommand();
                res.append("Server response to " + hostAndUserName + ": ");
            }

            if(requestString.startsWith("listports")) {
                requestString = requestString.substring("listports".length()).trim();
                command = new ListPortsCommand();
                res.append("Server response to " + hostAndUserName + ": ");
            }

            if(requestString.startsWith("!")) {
                requestString = requestString.substring(1).trim();
                command = new ExecuteCommand();
                res.append("Server response to " + hostAndUserName + ": ");
            }

            if(requestString.equalsIgnoreCase("register")) {
                requestString = hostAndUserName;
                command = new RegisterCommand(clients);
                res.append("Server response to " + hostAndUserName + ": ");
            }

            if(requestString.equalsIgnoreCase(
                    "list clients") || requestString.equalsIgnoreCase("listclients")
            ) {
                command = new ListClientsCommand(clients);
                res.append("Server response to " + hostAndUserName + ": ");
            }

            if(requestString.startsWith("to")) {
                requestString = hostAndUserName + " " + requestString;
                command = new SendToCommand(clients);
            }

            if(requestString.startsWith("BC") || requestString.startsWith("bc")) {
                requestString = requestString.substring(2).trim();
                command = new BroadcastCommand(clients);
            }


            if(requestString.equalsIgnoreCase("exit")) {
                stopExecutorService();
                serverShutdown();
            }


            command.execute(requestString, proxy);


        } else{
            return "Null request can't processed";
        }

        res.append(command.getOutput() + "\n");
        res.append("=====================================================");
        System.out.println("Server latency: " + command.getLatency() + " milliseconds");

        return res.toString();
    }

    private void stopExecutorService() {
        executorService.shutdown();
    }

    private void serverShutdown(){
        Server.shutdown();
    }
}
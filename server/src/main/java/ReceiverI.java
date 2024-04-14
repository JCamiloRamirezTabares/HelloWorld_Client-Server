import java.util.Arrays;
import java.util.concurrent.*;

import AppInterfaces.Receiver;
import AppInterfaces.RequesterPrx;
import com.zeroc.Ice.Current;
import commands.*;

public class ReceiverI implements Receiver
{
    private final ConcurrentHashMap<String, RequesterPrx> clients;
    private final ExecutorService executorService;
    private int totalRequests;
    private int unprocessed;
    private int processed;

    private long startTime;

    public ReceiverI(){
        totalRequests = 0;
        unprocessed = 0;
        processed = 0;

        clients = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
        startTime = System.currentTimeMillis() / 1000;
    }

    @Override
    public String printString(RequesterPrx requester, String s, Current current) {
        System.out.println(s);

        totalRequests++;
        processRequest(s, requester);


        return "";
    }

    private void processRequest(String s, RequesterPrx proxy) {
        System.out.println("Total requests received: " + totalRequests);
        CompletableFuture.supplyAsync(() -> constructResponse(s, proxy), executorService)
                .orTimeout(60, TimeUnit.SECONDS)
                .thenAccept(response -> {
                    proxy.printString(response);
                    processed++;
                    System.out.println("Total requests processed: " + processed);
                    System.out.println("Total requests unprocessed: " + unprocessed);

                    measuringAttributes();
                })
                .exceptionally(e -> {
                    unprocessed++;
                    if (e.getCause() instanceof TimeoutException) {
                        proxy.printString("Timeout occurred, unable to complete your request :c");
                    } else {
                        proxy.printString("Task cannot be processed: " + e.getMessage());
                    }
                    System.out.println("Total requests processed: " + processed);
                    System.out.println("Total requests unprocessed: " + unprocessed);

                    measuringAttributes();

                    return null;
                });
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


            command.execute(requestString, proxy);


        } else{
            return "Null request can't processed";
        }

        res.append(command.getOutput() + "\n");
        res.append("=====================================================");
        System.out.println("Server latency: " + command.getLatency() + " microseconds");

        return res.toString();
    }


    //Quality Attributes
    private double throughput(){
        long currentTime = System.currentTimeMillis() / 1000;
        double throughput = (double) processed / (currentTime - startTime);
        return Math.round(throughput * 1000) / 1000.0;
    }


    private double unprocessedRate(){
        double rate = (double) unprocessed / totalRequests;
        return Math.round(rate * 1000) / 1000.0;
    }

    //IsPending to Do
    private long missingRate(){
        return 1;
    }

    private void measuringAttributes(){
        System.out.println("Throughput: "+throughput() + " requests per second");
        System.out.println("Unprocessed Rate: "+unprocessedRate() * 100+"%");
        System.out.println("=====================================================");
    }



    private void stopExecutorService() {
        executorService.shutdown();
    }

    private void serverShutdown(){
        Server.shutdown();
    }
}
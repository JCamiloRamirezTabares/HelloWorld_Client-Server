import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.*;

import AppInterfaces.Receiver;
import AppInterfaces.RequesterPrx;
import com.zeroc.Ice.Current;
import commands.*;

public class ReceiverI implements Receiver
{

    private int totalRequests;
    private int unprocessed;
    private int processed;

    private long startTime;
    private final ConcurrentHashMap<String, RequesterPrx> clients;
    private final ExecutorService executorService;
    private final String path = "serverReport.txt";

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
       if(s != null || requester != null){
           System.out.println(s);

           totalRequests++;
           processRequest(s, requester);

           return "";
       } else{return "Null request";}
    }

    private void processRequest(String s, RequesterPrx proxy) {
        CompletableFuture.supplyAsync(() -> constructResponse(s, proxy), executorService)
                .orTimeout(60, TimeUnit.SECONDS)
                .thenAccept(response -> {
                    proxy.printString(response);
                    processed++;

                    String report = serverReport();
                    System.out.println(report);

                })
                .exceptionally(e -> {
                    unprocessed++;
                    if (e.getCause() instanceof TimeoutException) {
                        proxy.printString("("+s+") ==> " +
                                "Server Response: Timeout occurred, unable to complete your request :c\n" +
                                "=====================================================\n");
                    } else {
                        proxy.printString("("+s+") ==> " +
                                "Server Response: Task cannot be processed: " + e.getMessage() + "\n" +
                                "=====================================================\n");
                    }

                    String report = serverReport();
                    System.out.println(report);

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
                res.append("Fibonacci ("+requestString+") ==> Server response to "+hostAndUserName+": ");
            } catch (Exception e) {}

            if(requestString.equalsIgnoreCase("listifs")){
                command = new ListIfsCommand();
                res.append("listifs ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.startsWith("listports")) {
                requestString = requestString.substring("listports".length()).trim();
                command = new ListPortsCommand();
                res.append("listports ("+requestString+") ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.startsWith("!")) {
                requestString = requestString.substring(1).trim();
                command = new ExecuteCommand();
                res.append("! ("+requestString+") ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.equalsIgnoreCase("register")) {
                requestString = hostAndUserName;
                command = new RegisterCommand(clients);
                res.append("register ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.equalsIgnoreCase(
                    "list clients") || requestString.equalsIgnoreCase("listclients")
            ) {
                command = new ListClientsCommand(clients);
                res.append("list clients ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.startsWith("to")) {
                requestString = hostAndUserName + " " + requestString;
                command = new SendToCommand(clients);
                res.append(requestString+") ==> Server response to "+hostAndUserName+": ");
            }

            if(requestString.startsWith("BC") || requestString.startsWith("bc")) {
                requestString = requestString.substring(2).trim();
                command = new BroadcastCommand(clients);
                res.append("BC ("+requestString+") ==> Server response to "+hostAndUserName+": ");
            }


            command.execute(requestString, proxy);


        } else{
            return "Null request can't processed";
        }

        res.append(command.getOutput() + "\n");
        res.append("=====================================================\n");
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

    private String measuringAttributes(){
        return "Throughput: "+throughput() + " requests per second\n" +
                "Unprocessed Rate: "+unprocessedRate() * 100+"%\n" +
                "=====================================================";
    }

    private String serverReport(){
        String report = "Total requests received: " + totalRequests +"\n" +
                        "Total requests processed: " + processed +"\n" +
                        "Total requests unprocessed: " + unprocessed +"\n\n" +
                        "Measuring Attributes: \n" +
                        measuringAttributes();

        try {
            FileWriter writer = new FileWriter(path, false);
            writer.write(report);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error handling the FileWriter: " + e.getMessage());
        }

        return report;
    }

    public void closeExecutorService(){
        executorService.shutdown();
    }

}
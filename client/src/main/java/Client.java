
import java.io.*;
import java.lang.Exception;
import java.util.Random;

import com.zeroc.Ice.*;

import AppInterfaces.ReceiverPrx;
import AppInterfaces.RequesterPrx;
import com.zeroc.Ice.Object;

public class Client
{

    private static String username;
    private static String hostname;
    private static RequesterPrx clientProxy;
    private static ReceiverPrx serverProxy;

    private static long totalRequests;
    public static long startTime;


    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args)
    {
        initializeClient(args);
    }

    public static void initializeClient(String[] args){
        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            totalRequests = 0;
            username = System.getProperty("user.name");
            hostname = java.net.InetAddress.getLocalHost().getHostName();

            createServerPrx(communicator);
            createClientPrx(communicator);

            if(args.length >= 2 && args[0].equals("spm")){
                long number_requests;
                long number;
                try{
                    number_requests = Long.parseLong(args[1]);
                    number = Long.parseLong(args[2]);
                } catch (NumberFormatException e){
                    number_requests = 1;
                    number = 21;
                }

                spammingRequests(number, number_requests);
            } else{
                processRequest();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createServerPrx(Communicator communicator){
        serverProxy = AppInterfaces.ReceiverPrx.checkedCast(
            communicator.propertyToProxy("Server.Proxy")).ice_twoway().ice_secure(false);

        if(serverProxy == null){
            throw new Error("Invalid proxy");
        }
    }

    private static void createClientPrx(Communicator communicator){
        ObjectAdapter adapter = communicator.createObjectAdapter("Client");
        Object servent = new RequesterI();
        adapter.add(servent, Util.stringToIdentity("Requester"));
        adapter.activate();

        clientProxy = RequesterPrx.checkedCast(
                adapter.createProxy(Util.stringToIdentity("Requester")).ice_twoway().ice_secure(false));
    }

    public static void processRequest() throws IOException {

        boolean sentinel = true;

        String hostAndUser = username + "@" + hostname;

        System.out.println(welcome(hostAndUser));

        while(sentinel){
            System.out.println(menu());
            String line = reader.readLine();

            String request = hostAndUser+": "+line;

            if(!line.equalsIgnoreCase("exit")){
                sendRequest(request);
            } else{
                sentinel = false;
                System.out.println("See you later!!");
            }
        }

        RequesterI.write("Total requests sent: " + totalRequests);
        RequesterI.closeFileWriter();
    }

    private static void spammingRequests(long number, long number_requests) {

        Random random = new Random();
        int max = 57;
        int min = 20;

        String hostAndUser = username + "@" + hostname;

        for (int i = 1; i <= number_requests; i++){
            long numb = number;
            String request = hostAndUser+": "+numb;

            sendRequest(request);
        }

        RequesterI.write("Total requests sent: " + totalRequests);
        RequesterI.closeFileWriter();
    }

    private static void sendRequest(String request){
        startTime = System.nanoTime() / 1000;
        serverProxy.printString(clientProxy, request);
        totalRequests++;
    }

    private static String welcome(String userHost){
        return  "=============================================\n" +
                "||          Hello "+userHost+"!!!           ||\n" +
                "||        Welcome to HelloWorld app        ||\n" +
                "=============================================";
    }

    private static String menu(){
        return "Please enter a command or exit to (really? exit to exit)";
    }
}
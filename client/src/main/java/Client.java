
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

import com.zeroc.Ice.*;

import AppInterfaces.ReceiverPrx;
import AppInterfaces.RequesterPrx;

public class Client
{
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static String username;
    private static String hostname;
    private static RequesterPrx clientProxy;
    private static ReceiverPrx serverProxy;

    public static void main(String[] args)
    {
        initializeClient(args);
    }

    public static void initializeClient(String[] args){
        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            createServerPrx(communicator);
            processRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createServerPrx(Communicator communicator){
        serverProxy = AppInterfaces.ReceiverPrx.checkedCast(
            communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);

        if(serverProxy == null){
            throw new Error("Invalid proxy");
        }
    }

    public void createClientPrx(Communicator communicator){

    }

    public static void processRequest() throws IOException {

        boolean sentinel = true;

        username = System.getProperty("user.name");
        hostname = java.net.InetAddress.getLocalHost().getHostName();
        String hostAndUser = username + "@" + hostname + ": ";

        while(sentinel){
            System.out.print(hostAndUser);
            String line = reader.readLine();

            String request = hostAndUser + line;

            if(!line.equalsIgnoreCase("exit")){
                Runnable runnable = () -> sendRequest(request);
                Thread thread = new Thread(runnable);
                thread.start();
            } else{sentinel = false;}
        }
    }

    private static void sendRequest(String request){
        String res = serverProxy.printString(clientProxy, request);
        System.out.println(res);
    }
}
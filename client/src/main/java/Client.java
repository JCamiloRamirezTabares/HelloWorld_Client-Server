
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

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


    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args)
    {
        initializeClient(args);
    }

    public static void initializeClient(String[] args){
        try(Communicator communicator = Util.initialize(args, "config.client"))
        {
            createServerPrx(communicator);
            createClientPrx(communicator);
            processRequest();
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

        username = System.getProperty("user.name");
        hostname = java.net.InetAddress.getLocalHost().getHostName();

        while(sentinel){
            String hostAndUser = username + "@" + hostname + ": ";
            System.out.print(hostAndUser);
            String line = reader.readLine();

            String request = hostAndUser + line;

            if(!line.equalsIgnoreCase("exit")){
                sendRequest(request);
            } else{
                sentinel = false;
                sendRequest(request);
            }
        }
    }

    private static void sendRequest(String request){
        serverProxy.printString(clientProxy, request);
    }
}
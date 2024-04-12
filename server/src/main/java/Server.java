import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

public class Server
{

    private static Communicator communicator;
    public static void main(String[] args)
    {
        initializeServer(args);
    }


    public static void initializeServer(String[] args){
        try(Communicator c = Util.initialize(args,"config.server"))
        {
            communicator = c;
            createAdapter(communicator);
            System.out.println("Server has been started");

            communicator.waitForShutdown();
        }
    }

    public static void createAdapter(Communicator communicator){
        ObjectAdapter adapter = communicator.createObjectAdapter("Server");
        Object servent = new ReceiverI();
        adapter.add(servent, Util.stringToIdentity("Receiver"));
        adapter.activate();
    }

    public static void shutdown(){
        if (communicator != null) {
            communicator.shutdown();
        }
    }

}
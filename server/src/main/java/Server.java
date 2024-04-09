import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

public class Server
{
    public static void main(String[] args)
    {
        initializeServer(args);
    }


    public static void initializeServer(String[] args){
        try(Communicator communicator = Util.initialize(args,"config.server"))
        {
            createAdapter(communicator);
            System.out.println("Server has been started");

            communicator.waitForShutdown();
        }
    }

    public static void createAdapter(Communicator communicator){
        ObjectAdapter adapter = communicator.createObjectAdapter("Printer");
        Object servent = new ReceiverI();
        adapter.add(servent, Util.stringToIdentity("SimplePrinter"));
        adapter.activate();
    }

}
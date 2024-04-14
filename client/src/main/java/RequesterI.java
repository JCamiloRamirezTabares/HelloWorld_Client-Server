import AppInterfaces.Requester;
import com.zeroc.Ice.Current;

public class RequesterI implements Requester {
    @Override
    public void printString(String s, Current current) {
        long endTime = System.nanoTime() / 1000;
        System.out.println(s);

        System.out.println("Client Latency: " + (endTime - Client.startTime) + " microseconds");
        System.out.println("=====================================================");
    }
}

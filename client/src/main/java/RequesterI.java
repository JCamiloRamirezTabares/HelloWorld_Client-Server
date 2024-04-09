import AppInterfaces.Requester;
import com.zeroc.Ice.Current;

public class RequesterI implements Requester {
    @Override
    public void printString(String s, Current current) {
        System.out.println(s);
    }
}

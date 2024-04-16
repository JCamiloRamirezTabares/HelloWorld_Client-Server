import AppInterfaces.Requester;
import com.zeroc.Ice.Current;

import java.io.FileWriter;
import java.io.IOException;

public class RequesterI implements Requester {

    private static FileWriter fw;
    private final String path = "clientResponse.txt";

    public RequesterI() {
        try {
            fw = new FileWriter(path, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printString(String s, Current current) {
        long endTime = System.nanoTime() / 1000;
        System.out.println(s);
        try {
            fw.write(s);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Client Latency: " + (endTime - Client.startTime) + " microseconds");
        System.out.println("=====================================================");
    }

    public static void write(String string){
        try {
            fw.write(string);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeFileWriter() {
        try {
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

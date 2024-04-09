package commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ListPortsCommand extends Command{

    @Override
    public void excecuteCommand(String s) {
        StringBuilder out = new StringBuilder();

        try {
            InetAddress inetAddress = InetAddress.getByName(s);
            List<Integer> openPorts = scanOpenPorts(inetAddress);

            if (openPorts.isEmpty()) {
                out.append("Ports aren't found for IP " + s);
            } else {
                out.append("Open Ports for IP " + s + ": ");
                out.append(formatOpenPorts(openPorts));
            }
        } catch (Exception e) {
            out.append("Error scanning ports: " + e.getMessage());
        }

        setOutput(out.toString());

    }

    private List<Integer> scanOpenPorts(InetAddress inetAddress) {
        List<Integer> openPorts = new ArrayList<>();
        int timeout = 200; //

        for (int port = 1; port <= 65535; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(inetAddress, port), timeout);
                openPorts.add(port);
            } catch (IOException ex) {
                //"Puerto no abierto, lanzar excepcion y manejarla"
            }
        }

        return openPorts;
    }

    private String formatOpenPorts(List<Integer> openPorts) {
        StringBuilder formattedPorts = new StringBuilder();
        for (Integer port : openPorts) {
            formattedPorts.append(port).append(" ");
        }
        return formattedPorts.toString();
    }

}

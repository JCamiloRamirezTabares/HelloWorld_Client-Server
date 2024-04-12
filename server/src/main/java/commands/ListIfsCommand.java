package commands;

import AppInterfaces.RequesterPrx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ListIfsCommand extends Command{

    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        StringBuilder out = new StringBuilder();
        String line;
        try {
            Process p;
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                p = Runtime.getRuntime().exec("ipconfig");
            } else {
                p = Runtime.getRuntime().exec("ifconfig");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

            while ((line = br.readLine()) != null) {
                out.append(line).append("\n");
            }
            br.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            setOutput("Error fetching logical interfaces.");
        }
        

        setOutput(out.toString());
    }
    
}

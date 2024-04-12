package commands;

import AppInterfaces.RequesterPrx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ExecuteCommand extends Command{
    
    @Override
    protected void executeCommand(String s, RequesterPrx proxy) {
        StringBuilder out = new StringBuilder();

        try {
            Process process = Runtime.getRuntime().exec(s);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            out.append("Error loading command: ").append(e.getMessage());
        }

        setOutput(out.toString());
    }

}

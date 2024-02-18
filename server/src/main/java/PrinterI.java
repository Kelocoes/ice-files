import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PrinterI implements Demo.Printer {

    private static final String listPortsCommand = "listports";
    private static final String doCommand = "!";

    public void printString(String s, com.zeroc.Ice.Current current) {
        int index = s.indexOf(" - ");
        String message = s.substring(index + 3);

        try {
            checkIfNaturalNumber(s, Integer.parseInt(message));
        } catch (NumberFormatException e) {
            if (message.equals("exit")) {
                System.exit(0);
            } else if (message.startsWith("listifs")) {
                printCommand(s, new String[] { "ipconfig" });
            } else if (message.startsWith("listports ")) {
                int pos = message.indexOf(listPortsCommand);
                String ip = message.substring(pos + listPortsCommand.length()).trim();
                printCommand(s, new String[] { "nmap", ip });
            } else if (message.startsWith(doCommand)) {
                printCommand(s, message.substring(1).split("\\s+"));
            } else {
                System.out.println(s);
            }
        }
    }

    private static void checkIfNaturalNumber(String s, int n) {
        System.out.println(s);
        if (n > 0) {
            primeFactors(n);
        }
    }

    private static void primeFactors(int n) {
        System.out.print("Factores primos: ");
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                System.out.print(i + " ");
                n = n / i;
            }
        }
        System.out.println("");
    }

    private static void printCommand(String s, String[] command) {
        try {
            System.out.println(s);
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            int result = process.waitFor();
            System.out.println(result);
        } catch (Exception errorConsole) {
            System.out.println("El comando es incorrecto");
        }
    }
}
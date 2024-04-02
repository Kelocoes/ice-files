import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import Demo.Response;

public class Client {
    public static void main(String[] args) throws IOException {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client",
                extraArgs)) {
            Response response = null;
            Demo.PrinterPrx service = Demo.PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            if (service == null) {
                throw new Error("Invalid proxy");
            }

            /*
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
                }

                String user = System.getProperty("user.name");
                String hostname = execReadToString("hostname").trim();
                long start = System.currentTimeMillis();
                response = service.printString(user + ":" + hostname + " " + line);
                Long responsetTime = System.currentTimeMillis() - start;
                System.out.println("Respuesta del servidor:");
                System.out.println(response.value);
                System.out.println("Tiempo de respuesta:");
                System.out.println(responsetTime+ "ms" + "\n");
            }
            */

            String user = System.getProperty("user.name");
            String hostname = execReadToString("hostname").trim();
            long start = System.currentTimeMillis();
            response = service.printString(user + ":" + hostname + " " + "15000");
            Long responsetTime = System.currentTimeMillis() - start;
            //System.out.println("Respuesta del servidor:");
            //System.out.println(response.value);
            System.out.println("Tiempo de respuesta:");
            System.out.println(responsetTime+ "ms" + "\n");
        }
    }

    public static String execReadToString(String execCommand) throws IOException {
        Process proc = Runtime.getRuntime().exec(execCommand);
        try (InputStream stream = proc.getInputStream()) {
            try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                return s.hasNext() ? s.next() : "";
            }
        }
    }
}
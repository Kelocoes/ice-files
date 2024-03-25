import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackPrx;
import Demo.Response;

public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client",
                extraArgs)) {
            // com.zeroc.Ice.ObjectPrx base =
            // communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx service = Demo.PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            if (service == null) {
                throw new Error("Invalid proxy");
            }

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback");
            CallbackImp callbackImp = new CallbackImp();
            ObjectPrx obprx = adapter.add(callbackImp, Util.stringToIdentity("callbackClient"));
            adapter.activate();

            Scanner scanner = new Scanner(System.in);
            while (true) {

                CallbackPrx prx = CallbackPrx.uncheckedCast(obprx);

                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
                }

                String user = System.getProperty("user.name");
                String hostname = "";
                try {
                    hostname = execReadToString("hostname").trim();
                } catch (IOException e) {
                    hostname = "null";
                }

                service.printString(user + ":" + hostname + " " + line, prx);
                System.out.println("Request sent to server. Waiting for response...");
            }

            communicator.waitForShutdown();
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

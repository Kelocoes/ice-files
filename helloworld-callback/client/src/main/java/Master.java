import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.zeroc.Ice.Util;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;

import Demo.CallbackPrx;
import Demo.Response;

public class Master {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "config.client", extraArgs)) {
            // com.zeroc.Ice.ObjectPrx base =
            // communicator.stringToProxy("SimplePrinter:default -p 10000");
            Demo.PrinterPrx service = Demo.PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy"));

            if (service == null) {
                throw new Error("Invalid proxy");
            }

            String user = System.getProperty("user.name");
            String hostname = "";
            try {
                hostname = execReadToString("hostname").trim();
            } catch (IOException e) {
                hostname = "null";
            }

            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Callback", "tcp -p " + args[0] + " -h " + args[1]);
            CallbackImp callbackImp = new CallbackImp();
            ObjectPrx obprx = adapter.add(callbackImp, Util.stringToIdentity("callbackClient"));
            adapter.activate();

            CallbackPrx prx = CallbackPrx.uncheckedCast(obprx);
            service.subscribe(user + ":" + hostname, prx);

            Scanner scanner = new Scanner(System.in);
            while (true) {

                prx = CallbackPrx.uncheckedCast(obprx);

                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
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

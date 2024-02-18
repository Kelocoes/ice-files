import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client",
                extraArgs)) {
            Demo.PrinterPrx twoway = Demo.PrinterPrx.checkedCast(communicator.propertyToProxy("Printer.Proxy"))
                    .ice_twoway().ice_secure(false);
            Demo.PrinterPrx printer = twoway.ice_oneway();

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
                }

                String user = System.getProperty("user.name");
                String hostname = System.getenv("HOSTNAME");
                printer.printString(user + ":" + hostname + " - " + line);
            }
        }
    }
}
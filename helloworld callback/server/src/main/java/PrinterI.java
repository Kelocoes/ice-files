// import java.util.concurrent.Semaphore;

// import com.zeroc.IceInternal.ThreadPool;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Demo.Response;

public class PrinterI implements Demo.Printer {
    // ThreadPool ca;
    // Semaphore s = new Semaphore(2)
    private static final String listifsCommand = "listifs";
    private static final String listPortsCommand = "listports";
    private static final String ipconfigCommand = "ifconfig";
    private static final String nmapCommand = "nmap";
    private static final String doCommand = "!";

    public synchronized void printString(String s, Demo.CallbackPrx callback, com.zeroc.Ice.Current current) {
        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                System.out.println(s);
                int index = s.indexOf(" ");
                String message = s.substring(index + 1);
                String serverResponse = message;

                try {
                    serverResponse = checkIfNaturalNumber(s, Integer.parseInt(message));
                    Thread.sleep(5000);
                } catch (NumberFormatException e) {
                    serverResponse = handleNonNumericInput(s, message);
                }

                // System.out.println(serverResponse);
                callback.callbackClient(new Response(serverResponse, System.currentTimeMillis() - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static String checkIfNaturalNumber(String s, int n) {
        if (n > 0) {
            StringBuilder series = new StringBuilder();
            long[] fibonacciArray = new long[n];
            for (int i = 0; i < n; i++) {
                fibonacciArray[i] = fibonacci(i, new long[n]);
                series.append(fibonacciArray[i]).append(" ");
            }
            return series.toString();
        } else {
            return String.valueOf(n);
        }
    }

    public static long fibonacci(int num, long[] memo) {
        if (num == 0 || num == 1) {
            return num;
        }
        if (memo[num] != 0) {
            return memo[num];
        } else {
            memo[num] = fibonacci(num - 1, memo) + fibonacci(num - 2, memo);
            return memo[num];
        }
    }

    private String handleNonNumericInput(String s, String message) {
        String output = "";
        if (message.startsWith(listifsCommand)) {
            output = printCommand(new String[] { ipconfigCommand });
        } else if (message.startsWith(listPortsCommand)) {
            output = handleListPortsCommand(s, message);
        } else if (message.startsWith(doCommand)) {
            output = printCommand(message.substring(1).split("\\s+"));
        } else {
            output = message;
        }

        return output;
    }

    private static String printCommand(String[] command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String linea;
            StringBuilder series = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                series.append(linea).append("\n");
            }

            int result = process.waitFor();
            return series.toString();
        } catch (Exception errorConsole) {
            return "El comando es incorrecto";
        }
    }

    private String handleListPortsCommand(String s, String message) {
        int pos = message.indexOf(listPortsCommand);
        if (pos != -1) {
            String ip = message.substring(pos + listPortsCommand.length()).trim();
            return printCommand(new String[] { nmapCommand, ip });
        } else {
            return message;
        }
    }
}
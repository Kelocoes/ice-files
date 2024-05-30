import java.util.HashMap;
import java.util.Map;

import Demo.CallbackPrx;
import Demo.Response;

import com.zeroc.Ice.Current;

public class PrinterI implements Demo.Printer {
    // ThreadPool ca;

    Map<String, Demo.CallbackPrx> callbackWorkersList = new HashMap<>();

    PrinterI() {
        callbackWorkersList = new HashMap<>();
    }

    public void connectToMaster(String s, CallbackPrx callback, Current current) {
        this.callbackWorkersList.put(s, callback);
        callback.callbackClient(new Response("Connected to the Master", 0));
    }

    public synchronized void printString(String s, CallbackPrx callback, Current current) {
        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                int index = s.indexOf(" ");
                String message = s.substring(index + 1);
                String serverResponse = message;

                callback.callbackClient(new Response(serverResponse, System.currentTimeMillis() - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
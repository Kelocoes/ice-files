import java.util.HashMap;
import java.util.Map;

import Demo.WorkerPrx;

import com.zeroc.Ice.Current;

import java.util.Scanner;

public class MasterImp implements Demo.Master {
    // ThreadPool ca;

    Map<String, Demo.WorkerPrx> callbackWorkersList = new HashMap<>();

    MasterImp() {
        callbackWorkersList = new HashMap<>();
    }

    public void connectToMaster(String s, WorkerPrx worker, Current current) {
        this.callbackWorkersList.put(s, worker);
        worker.connectionResponse("Connected to the Master");
    }

    public void readCommandLine() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                scanner.close();
                break;
            }

            for (String worker : callbackWorkersList.keySet()) {
                try {
                    callbackWorkersList.get(worker).getTask("functionloca", 1, 2, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
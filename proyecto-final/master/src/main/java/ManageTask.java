import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Demo.WorkerPrx;
import Demo.ManageTaskPrx;

import com.zeroc.Ice.Current;

public class ManageTask implements Demo.ManageTask {

    List<Map.Entry<String, WorkerPrx>> callbackWorkersList = new ArrayList<>();
    ManageTaskPrx manageTaskPrx;
    double partialResults = 0;
    long totalTime = 0;

    ManageTask() {
        callbackWorkersList = new ArrayList<>();
    }

    public void setManageTaskPrx(ManageTaskPrx prx) {
        System.out.println("Master saved");
        manageTaskPrx = prx;
    }

    public ManageTaskPrx connectWorker(String s, WorkerPrx worker, Current current) {
        callbackWorkersList.add(new java.util.AbstractMap.SimpleEntry<>(s, worker));
        System.out.println("Worker " + s + " connected");
        worker.connectionResponse("Connected to the Master");
        return manageTaskPrx;
    }

    public void sendIntegral(String function, double initialPoint, double finalPoint, int workers, int cantNum) {
        new Thread(() -> {
            double interval = (finalPoint - initialPoint) / workers;
            double start = initialPoint;
            double end = start + interval;

            for (int i = 0; i < workers; i++) {
                Map.Entry<String, WorkerPrx> entry = callbackWorkersList.get(i);
                entry.getValue().getTask(function, start, end, cantNum);
                start = end;
                end = start + interval;
            }
        }).start();
    }

    public void addPartialResult(double partialResult, long time, Current current) {
        System.out.println("Partial result received: " + partialResult);
        System.out.println("Time: " + time + " ms");
        partialResults += partialResult;
        totalTime += time;
        System.out.println("Total partial result until now: " + partialResults);
        System.out.println("Total time until now: " + totalTime + " ms");
    }
}

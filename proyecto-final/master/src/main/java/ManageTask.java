import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.AbstractMap;

import Demo.WorkerPrx;
import Demo.ManageTaskPrx;

import com.zeroc.Ice.Current;

public class ManageTask implements Demo.ManageTask {

    List<Map.Entry<String, WorkerDetails>> callbackWorkersList = new ArrayList<>();
    ManageTaskPrx manageTaskPrx;
    int taskIndex = 0;
    // List of partial results for each task, double is the partial result and long is the time when started
    List<Map.Entry<Double, Long>> partialResults = new ArrayList<>();
    // List of workers finished for each task, Integer is the total workers and Integer is the workers finished
    List<Map.Entry<Integer, Integer>> workersFinished = new ArrayList<>();

    ManageTask() {
        callbackWorkersList = new ArrayList<>();
    }

    public void setManageTaskPrx(ManageTaskPrx prx) {
        System.out.println("Master saved");
        manageTaskPrx = prx;
    }

    public ManageTaskPrx connectWorker(String s, WorkerPrx worker, Current current) {
        callbackWorkersList.add(new java.util.AbstractMap.SimpleEntry<>(s, new WorkerDetails(worker, false)));
        System.out.println("Worker " + s + " connected");
        worker.connectionResponse("Connected to the Master");
        return manageTaskPrx;
    }

    public void disconnectWorker(String workerId, Current current) {
        for (Map.Entry<String, WorkerDetails> entry : callbackWorkersList) {
            if (entry.getKey().equals(workerId)) {
                callbackWorkersList.remove(entry);
                System.out.println("Worker " + workerId + " disconnected");
                break;
            }
        }
    }

    public Thread sendIntegral(String function, double initialPoint, double finalPoint, int workers, int cantNum) {
        Thread thread = new Thread(() -> {
            partialResults.add(new java.util.AbstractMap.SimpleEntry<>(0.0, System.currentTimeMillis()));
            double interval = (finalPoint - initialPoint) / workers;
            double start = initialPoint;
            double end = start + interval;
            List<Map.Entry<String, WorkerDetails>> availableWorkers = getAvailableWorkers(workers);
            workersFinished.add(new java.util.AbstractMap.SimpleEntry<>(workers, 0));

            if (availableWorkers.size() == workers) {
                for (Map.Entry<String, WorkerDetails> entry : availableWorkers) {
                    WorkerDetails workerDetails = entry.getValue();
                    workerDetails.getWorkerPrx().getTask(function, start, end, cantNum, this.taskIndex);
                    workerDetails.setBusy(true);
                    start = end;
                    end = start + interval;
                }
                this.taskIndex += 1;
            } else {
                System.out.println("Not enough workers available, try again with less workers.");
            }
        });
        thread.start();
        return thread;
    }

    private List<Map.Entry<String, WorkerDetails>> getAvailableWorkers(int workers) {
        List<Map.Entry<String, WorkerDetails>> availableWorkers = new ArrayList<>();
        for (Map.Entry<String, WorkerDetails> entry : callbackWorkersList) {
            WorkerDetails workerDetails = entry.getValue();
            if (!workerDetails.getBusy()) {
                availableWorkers.add(entry);
            }

            if (availableWorkers.size() == workers) {
                break;
            }
        }
        return availableWorkers;
    }

    public void addPartialResult(double partialResult, long time, int taskIndex, String workerId, Current current) {
        System.out.println("---------------Results from worker " + workerId + "-----------------");
        System.out.println("--> Partial result received for task " + taskIndex + ": " + partialResult);
        System.out.println("--> Time for task " + taskIndex + ": " + time + " ms");
        Map.Entry<Double, Long> newPartialResult = new AbstractMap.SimpleEntry<>(partialResults.get(taskIndex).getKey() + partialResult, partialResults.get(taskIndex).getValue());
        partialResults.set(taskIndex, newPartialResult);
        System.out.println("--> Total partial result until now for task " + taskIndex + ": " + partialResults.get(taskIndex).getKey());
        Map.Entry<Integer, Integer> newWorkersFinished = new AbstractMap.SimpleEntry<>(workersFinished.get(taskIndex).getKey(), workersFinished.get(taskIndex).getValue() + 1);
        workersFinished.set(taskIndex, newWorkersFinished);
        if (workersFinished.get(taskIndex).getValue() == workersFinished.get(taskIndex).getKey()) {
            long finishTime = System.currentTimeMillis();
            System.out.println("--> Total time for task " + taskIndex + ": " + (finishTime - partialResults.get(taskIndex).getValue()) + " ms");
        }
        System.out.println("------------------------------------------------------------");

        resetWorker(workerId);
    }

    private void resetWorker(String workerId) {
        for (Map.Entry<String, WorkerDetails> entry : callbackWorkersList) {
            if (entry.getKey().equals(workerId)) {
                WorkerDetails workerDetails = entry.getValue();
                workerDetails.setBusy(false);
                break;
            }
        }
    }

    public void closeWorkers() {
        for (Iterator<Map.Entry<String, WorkerDetails>> iterator = callbackWorkersList.iterator(); iterator.hasNext();) {
            Map.Entry<String, WorkerDetails> entry = iterator.next();
            WorkerDetails workerDetails = entry.getValue();
            System.out.println("Disconnecting worker " + entry.getKey());
            iterator.remove();
            try {
                workerDetails.getWorkerPrx().closeWorker();
            } catch (Exception e) {
                continue;
            }
        }
        System.exit(0);
    }
}

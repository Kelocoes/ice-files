import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;
import Demo.MasterPrx;

public class MasterImp implements Demo.Master {

    List<Map.Entry<String, Demo.WorkerPrx>> callbackWorkersList = new ArrayList<>();
    MasterPrx masterPrx;
    double partialResults = 0;

    MasterImp() {
        callbackWorkersList = new ArrayList<>();
    }

    public void setMasterPrx(Demo.MasterPrx prx) {
        System.out.println("Master saved");
        masterPrx = prx;
    }

    public MasterPrx connectToMaster(String s, WorkerPrx worker, Current current) {
        callbackWorkersList.add(new java.util.AbstractMap.SimpleEntry<>(s, worker));
        System.out.println("Worker " + s + " connected");
        worker.connectionResponse("Connected to the Master");

        return masterPrx;
    }

    public void readCommandLine() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    scanner.close();
                    break;
                }
                
                System.out.println("\nEnter the function as in Java, example: x^2 + 2*x + 1");
                String function = scanner.nextLine();

                System.out.println("\nEnter the initial point of the definite integral:");
                double initialPoint = scanner.nextDouble();

                System.out.println("\nEnter the final point of the definite integral:");
                double finalPoint = scanner.nextDouble();

                System.out.println("\nEnter the number of workers:");
                int workers = scanner.nextInt();

                System.out.println("\nEnter the number of random numbers to generate per thread per worker:");
                int cantNum = scanner.nextInt();

                System.out.println("\n**Processing the integral of the function: " + function + " from " + initialPoint + " to "
                        + finalPoint + " with " + workers + " workers.**\n");

                if (workers > callbackWorkersList.size()) {
                    System.out.println("The number of workers is greater than the number of workers connected.");
                    System.out.println("The number of workers connected is: " + callbackWorkersList.size());
                    System.out.println("Please connect more workers.");
                } else {
                    sendPartialIntegralsToWorkers(function, initialPoint, finalPoint, workers, cantNum);
                }
            } catch (Exception e) {
                System.out.println("You have made a mistake, please try again.");
            }
        }
    }

    private void sendPartialIntegralsToWorkers(String function, double initialPoint, double finalPoint, int workers,
            int cantNum) {
        double interval = (finalPoint - initialPoint) / workers;
        double start = initialPoint;
        double end = start + interval;

        for (int i = 0; i < workers; i++) {
            Map.Entry<String, Demo.WorkerPrx> entry = callbackWorkersList.get(i);
            entry.getValue().getTask(function, start, end, cantNum);
            start = end;
            end = start + interval;
        }
    }

    public void addPartialResult(double partialResult, long time, Current current) {
        System.out.println("Partial result received: " + partialResult);
        System.out.println("Time: " + time + " ms");
        partialResults += partialResult;
        System.out.println("Total partial result until now: " + partialResults);
    }
}
